package Coreconponent;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class PromiseExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    // Standard Promise Completion
    Promise<String> promise = Promise.promise();
    promise.future()
      .onSuccess(result -> System.out.println("Success: " + result))
      .onFailure(error -> System.out.println("Failure: " + error.getMessage()));
    promise.complete("Hello, Vert.x!");

    // Demonstrating failure
    Promise<String> failedPromise = Promise.promise();
    failedPromise.future()
      .onSuccess(result -> System.out.println("This will not be printed"))
      .onFailure(error -> System.out.println("Failure captured: " + error.getMessage()));
    failedPromise.fail("Something went wrong!");

    // Using map transformation
   Promise<Integer> mappedPromise = Promise.promise();
    mappedPromise.future()
      .map(value -> value * 2)
      .onSuccess(result -> System.out.println("Mapped Result: " + result));
    mappedPromise.complete(10);

    // Using compose (chaining async tasks)
    Promise<String> mainPromise = Promise.promise();
    mainPromise.future()
      .compose(result -> {
        System.out.println("First promise completed with: " + result);
        Promise<String> nextPromise = Promise.promise();
        vertx.setTimer(1000, id -> nextPromise.complete(result + " -> Next Step"));
        return nextPromise.future();
      })
      .onSuccess(finalResult -> System.out.println("Final Result: " + finalResult));
    mainPromise.complete("Start");


    // Demonstrating tryComplete() and tryFail()
    Promise<String> tryPromise = Promise.promise();
    boolean isCompleted = tryPromise.tryComplete("First Attempt");
    System.out.println("TryComplete first attempt: " + isCompleted);

    boolean isCompletedAgain = tryPromise.tryComplete("Second Attempt");
    System.out.println("TryComplete second attempt: " + isCompletedAgain);  // This should be false

    // Trying failure (this will not work since it's already completed)
    boolean isFailed = tryPromise.tryFail("Forced Failure");
    System.out.println("TryFail attempt: " + isFailed);  // This should be false

    // Waiting simulation using event loop (Vert.x is non-blocking, no real wait())
    Promise<String> waitPromise = Promise.promise();
    vertx.setTimer(2000, id -> waitPromise.complete("Delayed Success"));
    waitPromise.future().onSuccess(System.out::println);
  }
}
