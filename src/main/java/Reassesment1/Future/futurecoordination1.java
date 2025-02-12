package Reassesment1.Future;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class futurecoordination1 {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    Promise<String> promise1 = Promise.promise();
    Promise<String> promise2 = Promise.promise();
    Promise<String> promise3 = Promise.promise();

    // Completing promises after some delay
    vertx.setTimer(1000, id -> promise1.complete("Promise 1 Done"));
    vertx.setTimer(2000, id -> promise2.complete("Promise 2 Done"));
    vertx.setTimer(3000, id -> promise3.fail("Promise 3 Failed"));

    // Future.all() - This will fail if any future fails
    CompositeFuture allFutures = Future.all(promise1.future(), promise2.future(), promise3.future());

    allFutures.onComplete(result -> {
      if (result.succeeded()) {
        System.out.println("All futures completed successfully");
      } else {
        System.out.println("One or more futures failed: " + result.cause());
      }
    });

    // Future.any() - This will complete when the first future succeeds
    CompositeFuture anyFuture = Future.any(promise1.future(), promise2.future(), promise3.future());
    anyFuture.onComplete(result -> {
      if (result.succeeded()) {
        System.out.println("At least one future succeeded: " + result.result());
      } else {
        System.out.println("All futures failed");
      }
    });

    // Future.join() - This expects all futures to complete, ignoring failures (WRONG USAGE)
    CompositeFuture joinedFuture = Future.join(promise1.future(), promise2.future(), promise3.future());
    joinedFuture.onComplete(result -> {
      if (result.succeeded()) {
        System.out.println("All futures joined successfully: " + result.result());
      } else {
        System.out.println("Error joining futures: " + result.cause());
      }
    });
  }
}

