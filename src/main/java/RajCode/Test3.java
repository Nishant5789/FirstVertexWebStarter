package RajCode;

import com.sun.source.tree.UsesTree;
import io.vertx.core.*;

public class Test3 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws InterruptedException {
    Thread.sleep(5000);
    vertx.setTimer(1000, l -> {
      startPromise.complete();
      vertx.setPeriodic(200, id -> {
        System.out.println(UsesTree.class.getSimpleName());
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          try {
            Thread.currentThread().interrupt();
          } catch (Exception es) {
            System.out.println("caught exception");
            if (es instanceof InterruptedException) {
              Thread.currentThread().interrupt();
            }
          }
          throw new RuntimeException(e);
        }
      });
    });
    System.out.println(Thread.currentThread().getName() + " - MainVerticle started");

    // Deploy WorkerVerticle as a worker thread
    vertx.deployVerticle(new WorkerVerticle(), new DeploymentOptions(), res -> {
      if (res.succeeded()) {
        System.out.println(Thread.currentThread().getName() + "deployed worker verticle" + res.result());
      } else {
        System.out.println("Failed to deploy WorkerVerticle: " + res.cause());
      }
    });
    startPromise.complete();
//        vertx.close().onComplete(r->{
//            System.out.println("MainVerticle stopped");
//        }).onFailure(err->{
//           System.out.println("MainVerticle stopped");
//        });

  }

  public Future<String> hello1() {
    Promise<String> promise = Promise.promise();
    vertx.setTimer(6000, id -> {
      promise.complete("hello1");
    });
    return promise.future();
  }

  public Future<String> hello2() {
    Promise<String> promise = Promise.promise();
    vertx.setTimer(5000, id -> {
      promise.complete("hello2");
    });
    return promise.future();
  }

  public void stop() {
    System.out.println("stop");
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Test3());
  }
}

class WorkerVerticle extends AbstractVerticle {
  @Override
  public void start() {
    System.out.println("Worker Verticle started in thread: " + Thread.currentThread().getName());
    try {
      Thread.sleep(15);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Worker Verticle finished execution");
  }
}

