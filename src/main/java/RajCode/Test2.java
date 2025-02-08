package RajCode;

import io.vertx.core.*;

public class Test2 extends AbstractVerticle {

  public void start() {
    System.out.println(Thread.currentThread().getName());

    vertx.setTimer(1000,l->{
      System.out.println("timer over");
    });

    System.out.println(System.currentTimeMillis()/1000+" start");

    hello1().onComplete(r->{
      if(r.succeeded()){
        try {
          System.out.println(System.currentTimeMillis()/1000+" promise completed hello1 sleeping for 3 second ");
          Thread.sleep(5000);
          System.out.println(Thread.currentThread().getName()+ " " +System.currentTimeMillis()/1000);
          System.out.println("hello sleep complete");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      else{
        r.cause().printStackTrace();
      }
    });

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    hello2().onComplete(r->{
      if(r.succeeded()){
        try {
          System.out.println(System.currentTimeMillis()/1000+" promise completed hello2 sleeping for 5 second ");
          Thread.sleep(3000);
          System.out.println(Thread.currentThread().getName()+ " " +System.currentTimeMillis()/1000);
          System.out.println("hello2 sleep complete");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      else{
        r.cause().printStackTrace();
      }
    });

    System.out.println(System.currentTimeMillis()/1000);
    System.out.println("end");

  }

  public Future<String> hello1(){
    Promise<String> promise = Promise.promise();
    vertx.setTimer(3000, id -> {
      promise.complete("hello1");
    });
    return promise.future();
  }

  public Future<String> hello2(){
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
    vertx.deployVerticle(new Test2(), new DeploymentOptions().setThreadingModel(ThreadingModel.EVENT_LOOP));
  }
}
