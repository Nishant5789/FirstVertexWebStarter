package MCQPratice;

import io.vertx.core.*;
import java.util.List;

public class FutureCoordinationExample extends AbstractVerticle {

  @Override
  public void start() {
    System.out.println("Starting Verticle on Thread: " + Thread.currentThread().getName());

    // Creating multiple futures
    Future<String> future1 = delayedFuture("Task 1", 2000);
    Future<String> future2 = delayedFuture("Task 2", 3000);
    Future<String> future3 = delayedFuture("Task 3", 5000);
    Future<String> failingFuture = failingFuture("Task Failed");

    // Scenario 1: Future.all() - Waits for all to complete
    Future.all(List.of(future1, future2, future3)).onComplete(res -> {
      if (res.succeeded()) {
        System.out.println("✅ All tasks completed successfully: " + res.result());
      } else {
        System.out.println("❌ Some task failed: " + res.cause());
      }
    });

    // Scenario 2: Future.any() - Succeeds if any one future succeeds
    Future.any(List.of(future1, failingFuture)).onComplete(res -> {
      if (res.succeeded()) {
        System.out.println("✅ At least one task succeeded: " + res.result());
      } else {
        System.out.println("❌ All tasks failed: " + res.cause());
      }
    });

    // Scenario 3: Future.join() - Combines results
    Future.join(List.of(future1, failingFuture)).onComplete(res -> {
      if (res.succeeded()) {
        System.out.println("✅ Future.join() result: " + res.result());
      } else {
        System.out.println("❌ Future.join() failed: " + res.cause());
      }
    });

    // Scenario 4: Promise with Future
    Promise<String> promise = Promise.promise();
    vertx.setTimer(4000, id -> promise.complete("Promise Resolved after 4 seconds"));
    promise.future().onSuccess(result -> System.out.println("✅ Promise completed with: " + result));

    // Scenario 5: Using executeBlocking
    vertx.executeBlocking(promise1 -> {
        try {
          System.out.println(Thread.currentThread().getName());
          Thread.sleep(3000); // Simulate long-running task
          promise1.complete("Blocking task done!");
        } catch (InterruptedException e) {
          promise1.fail(e);
        }
      }).onSuccess(res -> System.out.println("✅ Blocking task result: " + res))
      .onFailure(err -> System.out.println("❌ Blocking task failed: " + err));
  }

  // Method to simulate a delayed Future
  private Future<String> delayedFuture(String message, long delay) {
    Promise<String> promise = Promise.promise();
    vertx.setTimer(delay, id -> promise.complete(message + " completed in " + delay + "ms"));
    return promise.future();
  }

  // Method to simulate a failing Future
  private Future<String> failingFuture(String message) {
    Promise<String> promise = Promise.promise();
    vertx.setTimer(2000, id -> promise.fail(message + " encountered an error"));
    return promise.future();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FutureCoordinationExample());
  }
}
