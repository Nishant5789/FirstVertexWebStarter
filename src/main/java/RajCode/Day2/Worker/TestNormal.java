package RajCode.Day2.Worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class TestNormal extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) {
    System.out.println(Thread.currentThread().getName() + " - test normal verticle deploy started");
//    vertx.executeBlocking(promise -> {
//      System.out.println(Thread.currentThread().getName() + " - execute blocking task started");

//      // Deploy a normal verticle from the worker thread
//        vertx.deployVerticle(new InnerTestNormal1(), new DeploymentOptions());

        vertx.deployVerticle(new InnerTestNormal2(), new DeploymentOptions());

//      System.out.println(Thread.currentThread().getName() + " - execute blocking task end");
//      promise.complete();
//    });
    System.out.println(Thread.currentThread().getName() + " - test normal verticle deploy end");
    startPromise.complete();
  }
}

class InnerTestNormal1 extends AbstractVerticle{
  @Override
  public void start() throws Exception {
    System.out.println(Thread.currentThread().getName() + " - test Innernormal verticle deploy started");
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }
}
class InnerTestNormal2 extends AbstractVerticle{
  @Override
  public void start() throws Exception {
    System.out.println(Thread.currentThread().getName() + " - test Inner normal verticle deploy started");
    Future<String> f1 = task1();
    Future<String> f2 = task2();
    vertx.executeBlocking(promise->{
      System.out.println(Thread.currentThread().getName()+" blocking operaton is started");
      Promise promise1 = Promise.promise();
      Promise promise2 = Promise.promise();
      vertx.setTimer(2000, id -> {
        System.out.println("blocking task1() resolved after " + 2000 + "ms | " + Thread.currentThread().getName());
        promise1.complete("blocking task completed");
      });
      vertx.setTimer(2000, id -> {
        System.out.println("blocking task2() resolved after " + 2000 + "ms | " + Thread.currentThread().getName());
        promise2.complete("blocking task completed");
      });
      vertx.executeBlocking(Promise->{
        vertx.setTimer(2000,id->{
          System.out.println("inner blocking task start executing : " + Thread.currentThread().getName());
          promise.complete("inner blocking task done : ");
        });
      });
//      System.out.println("end blocking task" + Thread.currentThread().getName());
    });

    f1.onComplete(ar->{
      if(ar.succeeded()){
        System.out.println("task1 is completed : " + Thread.currentThread().getName() );
      }
    });
    f2.onComplete(ar->{
      if(ar.succeeded()){
        System.out.println("task2 is completed : " + Thread.currentThread().getName() );
      }
    });

  }
  @Override
  public void stop() throws Exception {
    super.stop();
  }

  public Future<String> task1() {
    System.out.println(Thread.currentThread().getName() + " task1 is started ...");
    Promise<String> promise = Promise.promise();
    int delay = 2000;
    vertx.setTimer(delay, id -> {
      System.out.println("task1() resolved after " + delay + "ms | " + Thread.currentThread().getName());
      promise.complete("task1");
    });
    return promise.future();
  }
  public Future<String> task2() {
    System.out.println(Thread.currentThread().getName() + " task2 is started ...");
    Promise<String> promise = Promise.promise();
    int delay = 2000;
    vertx.setTimer(delay, id -> {
      System.out.println("task2() resolved after " + delay + "ms | " + Thread.currentThread().getName());
      promise.complete("task2");
    });
    return promise.future();
  }

}

