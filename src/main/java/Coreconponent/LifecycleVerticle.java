package Coreconponent;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;


public class LifecycleVerticle extends AbstractVerticle {
  // Called when the Verticle is deployed
  @Override
  public void start() {
    System.out.println("Verticle has started!");
  }

  // Called when the Verticle is stopped
  @Override
  public void stop() {
    System.out.println("Verticle has stopped!");
    // Perform cleanup logic here (e.g., releasing resources)
  }

  // Asynchronous start method with a Promise
  @Override
  public void start(Promise<Void> startPromise) {
    System.out.println("Verticle is starting asynchronously...");
    // Perform async initialization tasks
    vertx.setTimer(5000, id -> {
      System.out.println("Initialization complete!");
      startPromise.complete(); // Notify that the start is successful
    });
  }

  // Asynchronous stop method with a Promise
  @Override
  public void stop(Promise<Void> stopPromise) {
    System.out.println("Verticle is stopping asynchronously...");
    // Perform async cleanup tasks
    vertx.setTimer(1000, id -> {
      System.out.println("Cleanup complete!");
      stopPromise.complete(); // Notify that the stop is successful
    });
  }
}
