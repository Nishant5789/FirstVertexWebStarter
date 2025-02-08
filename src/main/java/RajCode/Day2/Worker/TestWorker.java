package RajCode.Day2.Worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class TestWorker extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) {
    System.out.println(Thread.currentThread().getName() + " - test worker verticle deploy started");

    vertx.executeBlocking(promise -> {
      System.out.println(Thread.currentThread().getName() + " - execute blocking task started");
      // Deploy a normal verticle from the worker thread

      vertx.deployVerticle(new TestNormal(), res -> {
        if (res.succeeded()) {
          System.out.println(Thread.currentThread().getName() + " - Deployed normal verticle"+" in TestWorker  Normal verticle");
        } else {
          System.out.println("Failed to deploy normal verticle: " + res.cause());
        }
      });

      System.out.println(Thread.currentThread().getName() + " - execute blocking task end");
      promise.complete();
    });
    System.out.println(Thread.currentThread().getName() + " - test worker verticle deploy end");
    startPromise.complete();
  }
}

