package RajCode.Day3;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class WorkerHandlerJainil extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerHandlerJainil(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));
  }

  @Override
  public void start() throws Exception {
    System.out.println("WorkerHandler started");
    EventBus eventBus = vertx.eventBus();
//
//    vertx.setTimer(5000, id -> {
//      System.out.println(Thread.currentThread().getName() + " sending event bus message");
//      eventBus.send("event.queue", new JsonObject().put("task", "1"));
//    });
//
//    vertx.setTimer(6000, id -> {
//      System.out.println(Thread.currentThread().getName() + " sending event bus message");
//      eventBus.send("event.queue", new JsonObject().put("task", "2"));
//    });
//
//    vertx.setTimer(6000, id -> {
//      System.out.println(Thread.currentThread().getName() + " sending event bus message");
//      eventBus.send("event.queue", new JsonObject().put("task", "3"));
//    });
//
//    vertx.setTimer(6000, id -> {
//      System.out.println(Thread.currentThread().getName() + " sending event bus message");
//      eventBus.send("event.queue", new JsonObject().put("task", "4"));
//    });
//
//    vertx.setTimer(6000, id -> {
//      System.out.println(Thread.currentThread().getName() + " sending event bus message");
//      eventBus.send("event.queue", new JsonObject().put("task", "4"));
//    });
    vertx.executeBlocking(promise -> {
      System.out.println(Thread.currentThread().getName() + " executing blocking task" + 1);
      try {
        Thread.sleep(4000);
        promise.complete();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }).onComplete(result -> {
      if (result.succeeded()) {
        System.out.println(Thread.currentThread().getName() + " executed blocking task" + 1);
      }
    });

    System.out.println(Thread.currentThread().getName() + " .blocking 1 reg successfully");

    vertx.executeBlocking(promise -> {
      System.out.println(Thread.currentThread().getName() + " executing blocking task 2");
      try {
        Thread.sleep(4000);
        promise.complete();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }).onComplete(result -> {
      if (result.succeeded()) {
        System.out.println(Thread.currentThread().getName() + " executed blocking task 2");
      }
    });
  }
}
