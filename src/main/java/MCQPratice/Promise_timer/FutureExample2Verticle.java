package MCQPratice.Promise_timer;

import io.vertx.core.*;

public class FutureExample2Verticle extends AbstractVerticle {

  @Override
  public void start() {
    executeTask()
      .compose(result -> processResult(result))  // Chain another future
      .onSuccess(finalResult -> System.out.println("✅ Success: " + finalResult))
      .onFailure(err -> System.out.println("❌ Failure: " + err.getMessage()))
      .onComplete(res -> System.out.println("🔄 Completed with: " + (res.succeeded() ? res.result() : res.cause())))
      .eventually(v -> {
        System.out.println("🧹 Cleaning up resources...");
        return Future.succeededFuture();
      })
      .otherwise(err -> {
        System.out.println("🚨 Providing default value due to error...");
        return "Default Value";
      })
      .flatMap(res -> Future.succeededFuture("🔄 Transformed: " + res))
      .toCompletionStage()
      .thenAccept(finalOutput -> System.out.println("🚀 Processed in CompletionStage: " + finalOutput));
  }

  private Future<String> executeTask() {
    Promise<String> promise = Promise.promise();

    vertx.setTimer(1000, id -> {
      if (Math.random() > 0.5) {
        promise.complete("🎉 Task Successful");
      } else {
        promise.fail("💥 Task Failed");
      }
    });

    return promise.future();
  }

  private Future<String> processResult(String input) {
    return Future.succeededFuture(input + " -> Processed");
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FutureExample2Verticle());
  }
}
