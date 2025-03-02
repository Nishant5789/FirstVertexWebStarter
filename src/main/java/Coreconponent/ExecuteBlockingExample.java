package Coreconponent;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;


public class ExecuteBlockingExample extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ExecuteBlockingExample());
  }

  @Override
  public void start() {
    System.out.println("🌟 Main Verticle Started!");

    // 1️⃣ Default executeBlocking without ordered flag
//    executeBlockingExample(false);
    // 2️⃣ executeBlocking with ordered = true

    // run concurrently
//    executeBlockingExample(false);
//    executeBlockingExample(false);

    // run sequentially(deafault)
//    executeBlockingExample(true);
//    executeBlockingExample(true);

    // 3️⃣ executeBlocking that fails
//    executeBlockingFailure();
    // 4️⃣ executeBlocking without result handler (Fire and Forget)
//    executeBlockingWithoutResultHandler();
  }

  // 1️⃣ & 2️⃣ Execute blocking with ordered or unordered execution
  private void executeBlockingExample(boolean ordered) {

    vertx.executeBlocking(promise -> {
      try {
        System.out.println("🔵 Executing Blocking Task (Ordered = " + ordered + ")... " + Thread.currentThread().getName());
        Thread.sleep(2000); // Simulate a heavy computation task
        promise.complete("✅ Task Completed (Ordered = " + ordered + ")");
      } catch (InterruptedException e) {
        promise.fail(e);
      }
    }, ordered, result -> {
      if (result.succeeded()) {
        System.out.println(result.result());
      } else {
        System.err.println("❌ Task Failed: " + result.cause().getMessage());
      }
    });
  }


  // 3️⃣ executeBlocking that fails intentionally
  private void executeBlockingFailure() {
    System.out.println("🔴 Executing Blocking Task that Fails...");

    vertx.executeBlocking(promise -> {
      promise.fail(new RuntimeException("Simulated Failure"));
    }, false, result -> {
      if (result.failed()) {
        System.err.println("❌ Failure Handled: " + result.cause().getMessage());
      }
    });
  }

  // 4️⃣ executeBlocking without result handler (Fire & Forget)
  private void executeBlockingWithoutResultHandler() {
    System.out.println("⚡ Fire & Forget Blocking Task Started...");

    vertx.executeBlocking(promise -> {
      try {
        Thread.sleep(1500); // Simulating task
        System.out.println("⚡ Fire & Forget Task Completed!");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, false);
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }
}

