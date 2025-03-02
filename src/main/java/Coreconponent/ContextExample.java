package Coreconponent;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

public class ContextExample extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ContextExample());
  }
  @Override
  public void start() {
    Context context = vertx.getOrCreateContext(); // Get current context

    // Store data in context
    context.put("message", "Hello from Vert.x Context!");
    // Run code within the same context
    context.runOnContext(v -> {
      String msg = context.get("message");
      System.out.println("✅ Retrieved Message: " + msg);
    });

    // Run another async task that shares the context
    vertx.setTimer(1000, id -> {
      String msg = context.get("message");
      System.out.println("⏳ Timer Retrieved Message: " + msg);
    });

    // Deploy another verticle to demonstrate shared context
    vertx.deployVerticle(new AnotherVerticle());
  }

  // Another verticle to demonstrate how context is separate for different verticles
  public static class AnotherVerticle extends AbstractVerticle {
    @Override
    public void start() {
      Context context = vertx.getOrCreateContext();
      String msg = context.get("message"); // Will return null because it's a different context
      System.out.println("🚀 Another Verticle Retrieved Message: " + msg);

      // Store data in this context
      context.put("anotherMessage", "Hello from AnotherVerticle!");

      context.runOnContext(v -> {
        System.out.println("🔵 AnotherVerticle Message: " + context.get("anotherMessage"));
      });
    }
  }
}

