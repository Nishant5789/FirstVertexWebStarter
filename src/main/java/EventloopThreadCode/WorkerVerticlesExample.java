package EventloopThreadCode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class WorkerVerticlesExample {
  public static void main(String[] args) throws InterruptedException {
    Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(10)); // Setting worker pool size to 10
    int numOfVerticles = 1000;
    CountDownLatch latch = new CountDownLatch(numOfVerticles);
    Map<String, Integer> threadCount = new HashMap<>();

    for (int i = 0; i < numOfVerticles; i++) {
      vertx.deployVerticle(new AbstractVerticle() {
        @Override
        public void start() {
          vertx.executeBlocking(promise -> {
            String threadName = Thread.currentThread().getName();
            threadCount.put(threadName, threadCount.getOrDefault(threadName, 0) + 1);
            promise.complete();
            latch.countDown();
          }, res -> {});
        }
      });
    }

    latch.await();
    System.out.println("Thread distribution for worker verticles:");
    threadCount.forEach((thread, count) -> System.out.println(thread + " = " + count));

    vertx.close();
  }
}
