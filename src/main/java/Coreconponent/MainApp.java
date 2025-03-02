package Coreconponent;

import io.vertx.core.Vertx;

public class MainApp {
  public static void main(String[] args) {
    // Create a Vert.x instance
    Vertx vertx = Vertx.vertx();

    // Deploy the LifecycleVerticle
    vertx.deployVerticle(new LifecycleVerticle(), res -> {
      if (res.succeeded()) {
        System.out.println("Verticle deployed successfully! Deployment ID: " + res.result());
      } else {
        System.out.println("Failed to deploy Verticle: " + res.cause().getMessage());
      }
    });
  }
}
