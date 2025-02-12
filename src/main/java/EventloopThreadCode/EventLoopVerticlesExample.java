package EventloopThreadCode;

import io.vertx.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class EventLoopVerticlesExample {
  public static void main(String[] args) throws InterruptedException {
    Vertx vertx = Vertx.vertx(new VertxOptions());
    int numOfVerticles = 1000;
    CountDownLatch latch = new CountDownLatch(numOfVerticles);

    Map<String, Integer> threadCount = new HashMap<>();

    for (int i = 0; i < numOfVerticles; i++) {
      vertx.deployVerticle(new AbstractVerticle() {
        @Override
        public void start() {
          String threadName = Thread.currentThread().getName();
          threadCount.put(threadName, threadCount.getOrDefault(threadName, 0) + 1);
          latch.countDown();
        }
      }, new DeploymentOptions().setThreadingModel(ThreadingModel.EVENT_LOOP));
    }

    latch.await();
    System.out.println("Thread distribution for event loop verticles:");
    threadCount.forEach((thread, count) -> System.out.println(thread + " = " + count));
    vertx.close();
  }
}
