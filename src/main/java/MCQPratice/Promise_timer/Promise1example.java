package MCQPratice.Promise_timer;

import io.vertx.core.*;

public class Promise1example {
  public static void main(String[] args) {

    Future<String> future = Future.succeededFuture("Done");
    future.onSuccess(res -> {
      System.out.println("Success: " + res);
      throw new RuntimeException("Oops");
    }).onFailure(err -> System.out.println("Failure: " + err.getMessage()));

//    VertxPromiseExceptionHandling();
//    Vertx vertx = Vertx.vertx();

//    Promise<String> promise = Promise.promise();
//    // Rejecting the promise
//    Future<String> future = promise.future();
//    promise.fail(new Throwable("The Fails!"));
////    promise.complete("sucess promise");

//    // Multiple handlers can handle the failure
//    future.onSuccess(res-> System.out.println(res.getBytes()));
//    future.onFailure(error -> System.out.println(error.getMessage()));
//    future.onFailure(error -> System.out.println(error.getMessage()));
////    future.onSuccess(res-> System.out.println(res.getBytes()));
////    future.onFailure(error -> System.out.println(error.getMessage()));

//    vertx.close();
  }

  public static void VertxPromiseExceptionHandling() {
    Vertx vertx = Vertx.vertx();

    Promise<String> promise = Promise.promise();
    promise.complete("Success!");

    Future<String> future = promise.future();

//    future.toCompletionStage()

//    future
//      .onSuccess(result -> {
//        System.out.println("Success Block: " + result);
//        throw new RuntimeException("Error inside onSuccess!");  // Throwing an exception
//      })
//      .onFailure(error -> System.out.println("Failure Block: " + error.getMessage()));  // Will NOT be called!


//    --- alternative -----
    future
      .compose(result -> {
        System.out.println("Success Block: " + result);
        return Future.failedFuture(new RuntimeException("Error inside onSuccess!"));
      })
      .onFailure(error -> System.out.println("Failure Block: " + error.getMessage()));  // Now it will be caught!
    vertx.close();
  }
}
