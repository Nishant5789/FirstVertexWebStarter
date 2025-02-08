package RajCode;

import io.vertx.core.*;
import java.util.Random;

public class Test4 extends AbstractVerticle {

  private final Random random = new Random();

  @Override
  public void start() {
    System.out.println(Thread.currentThread().getName() + " | Start method executing");

    vertx.setTimer(1000, l -> System.out.println("Timer Over | " + Thread.currentThread().getName()));

    long startTime = System.currentTimeMillis() / 1000;
    System.out.println(startTime + " | Start");

    // Introduce multiple async calls with CompositeFuture
    Future<String> f1 = hello1();
    Future<String> f2 = hello2();

    CompositeFuture.all(f1, f2).onComplete(result -> {
      if (result.succeeded()) {
        System.out.println(System.currentTimeMillis() / 1000 + " | All promises completed. Blocking main thread for 4 seconds...");
        try {
          Thread.sleep(4000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + " | Final execution | " + System.currentTimeMillis() / 1000);
      } else {
        result.cause().printStackTrace();
      }
    });

    System.out.println(System.currentTimeMillis() / 1000 + " | End of start() method");
  }

  public Future<String> hello1() {
    Promise<String> promise = Promise.promise();
    int delay = random.nextInt(4000) + 1000; // Random delay between 1s - 5s

    vertx.setTimer(2000, id -> {
      System.out.println("hello1() resolved after " + delay + "ms | " + Thread.currentThread().getName());
      promise.complete("hello1");
    });

    return promise.future().onSuccess(res -> {
      try {
        System.out.println(System.currentTimeMillis() / 1000 + " | hello1 sleeping for 2 seconds...");
        Thread.sleep(2000); // Blocking worker thread
        System.out.println(Thread.currentThread().getName() + " | hello1 sleep complete");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public Future<String> hello2() {
    Promise<String> promise = Promise.promise();
    int delay = random.nextInt(3000) + 2000; // Random delay between 2s - 5s

    vertx.setTimer(2000, id -> {
      System.out.println("hello2() resolved after " + delay + "ms | " + Thread.currentThread().getName());
      promise.complete("hello2");
    });

    return promise.future().onSuccess(res -> {
      try {
        System.out.println(System.currentTimeMillis() / 1000 + " | hello2 sleeping for 3 seconds...");
        Thread.sleep(3000); // Blocking worker thread
        System.out.println(Thread.currentThread().getName() + " | hello2 sleep complete");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public void stop() {
    System.out.println("Stopping verticle...");
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DeploymentOptions options = new DeploymentOptions()
      .setWorker(true);
    vertx.deployVerticle(new Test4(), options);
  }
}

