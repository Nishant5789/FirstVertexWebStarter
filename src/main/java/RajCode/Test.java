package RajCode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class Test extends AbstractVerticle {

  public void start() {
//        long currentTimeInSeconds = System.currentTimeMillis();
//        System.out.println(Thread.currentThread().getName()+"    "+currentTimeInSeconds);
    System.out.println(System.currentTimeMillis()/1000);
    System.out.println("start");
    System.out.println(System.currentTimeMillis()/1000);
//    System.out.println("calling hello method");


    hello1().onComplete(r->{
      if(r.succeeded()){
        try {
          System.out.println(System.currentTimeMillis()/1000);
          System.out.println("promise completed hello sleeping for 2 second");
          Thread.sleep(5000);
          System.out.println(System.currentTimeMillis()/1000);
          System.out.println("hello1 sleep complete");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      else{
        r.cause().printStackTrace();
      }
    });

//    System.out.println("calling hello2 method");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    hello2().onComplete(r->{
      if(r.succeeded()){
        try {
          System.out.println(System.currentTimeMillis()/1000);
          System.out.println("promise completed hello2 sleeping for 5 second ");
          Thread.sleep(3000);
          System.out.println(System.currentTimeMillis()/1000);
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
    vertx.setTimer(2000, id -> {
      promise.complete("hello");
    });
    return promise.future();
  }

  public Future<String> hello2(){
    Promise<String> promise = Promise.promise();
    vertx.setTimer(5000, id -> {
      promise.complete("hello1");
    });
    return promise.future();
  }

  public void stop() {
    System.out.println("stop");
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Test());
  }
}
