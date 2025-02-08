import io.vertx.core.*;

public class PromiseExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    Promise<String> promise = Promise.promise();

    // onSuccess and onFailure handling
    promise.future()
      .onSuccess(result -> System.out.println("Success: " + result))
      .onFailure(error -> System.out.println("Failure: " + error.getMessage()));

    // Completing successfully
    promise.complete("Hello, Vert.x!");

    // Another promise to demonstrate failure
    Promise<String> failedPromise = Promise.promise();
    failedPromise.future()
      .onSuccess(result -> System.out.println("This will not be printed"))
      .onFailure(error -> System.out.println("Failure captured: " + error.getMessage()));

    failedPromise.fail("Something went wrong!");

    // Another example using map transformation
    Promise<Integer> mappedPromise = Promise.promise();
    mappedPromise.future()
      .map(value -> value * 2)
      .onSuccess(result -> System.out.println("Mapped Result: " + result));

    mappedPromise.complete(10);

    // Chaining using compose (flatMap equivalent)
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
  }
}
