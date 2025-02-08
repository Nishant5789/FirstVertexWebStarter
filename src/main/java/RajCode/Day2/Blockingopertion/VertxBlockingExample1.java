package RajCode.Day2.Blockingopertion;

import io.vertx.core.*;

public class VertxBlockingExample1 extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new VertxBlockingExample1());
//    vertx.deployVerticle(new WorkerVerticle());
  }

  @Override
  public void start() {
    System.out.println("Main Verticle Started!");

    // 1️⃣ Execute a blocking task using executeBlocking()
    vertx.executeBlocking(promise -> {
      System.out.println("🔵 [executeBlocking] Task started...");
      try {
        Thread.sleep(3000); // Simulating long task
        promise.complete("🔵 [executeBlocking] Task Completed!");
      } catch (InterruptedException e) {
        promise.fail(e);
      }
    }, res -> {
      if (res.succeeded()) {
        System.out.println(res.result());
      } else {
        res.cause().printStackTrace();
      }
    });

    // 2️⃣ Using setTimer() for delayed execution
    vertx.setTimer(2000, id -> {
      System.out.println("⏳ [setTimer] Task executed after 2 seconds.");
    });

    // 3️⃣ Using Future & Promise for async execution
    asyncTask().onSuccess(result -> {
      System.out.println(result);
    }).onFailure(Throwable::printStackTrace);

    // 4️⃣ Sending a task to Worker Verticle
    vertx.eventBus().request("worker.queue", "Process this in Worker", reply -> {
      if (reply.succeeded()) {
        System.out.println("✅ [Worker Verticle] Response: " + reply.result().body());
      }
    });

    System.out.println("Main thread continues execution...");
  }

  // 3️⃣ Asynchronous task using Future & Promise
  private Future<String> asyncTask() {
    Promise<String> promise = Promise.promise();
    vertx.setTimer(1000, id -> {
      promise.complete("✅ [Future] Async Task Completed!");
    });
    return promise.future();
  }
}

// 4️⃣ Worker Verticle - Handling Blocking Code
class WorkerVerticle extends AbstractVerticle {
  @Override
  public void start() {
    vertx.eventBus().consumer("worker.queue", message -> {
      vertx.executeBlocking(promise -> {
        try {
          System.out.println("🟢 [Worker] Processing task...");
          Thread.sleep(5000);
          promise.complete("🟢 [Worker] Task Done!");
        } catch (InterruptedException e) {
          promise.fail(e);
        }
      }, res -> {
        message.reply(res.result());
      });
    });
  }
}
