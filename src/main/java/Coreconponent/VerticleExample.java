package Coreconponent;

import io.vertx.core.*;

public class VerticleExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    // Deploy standard verticle
    vertx.deployVerticle(new StandardVerticle());

    //  Deploy worker verticle
    DeploymentOptions workerOptions = new DeploymentOptions()
      .setInstances(1) // Two instances
      .setThreadingModel(ThreadingModel.WORKER);
    vertx.deployVerticle(new WorkerVerticle(), workerOptions);
  }

  // Standard Verticle
  public static class StandardVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
      vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello from Standard Verticle!"))
        .listen(8081)
        .<Void>mapEmpty()
        .onComplete(startPromise);
    }
  }


  // Worker Verticle
  public static class WorkerVerticle extends AbstractVerticle {
    @Override
    public void start() {
      vertx.executeBlocking(promise -> {
        // Simulate blocking operation
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          promise.fail(e);
        }
        promise.complete();
      }, res -> {
        if (res.succeeded()) {
          System.out.println("Worker Verticle started");
        }
      });
    }
  }

//  // Virtual Thread Verticle (Java 21+)
//  public static class VirtualThreadVerticle extends AbstractVerticle {
//    @Override
//    public void start(Promise<Void> startPromise) {
//      vertx.createHttpServer()
//        .requestHandler(req -> req.response().end("Hello from Virtual Thread Verticle!"))
//        .listen(8082)
//        .<Void>mapEmpty()
//        .onComplete(startPromise);
//    }
//  }
}

