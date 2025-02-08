package com.motadata.FirstWebStarter;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MainApp{
  public static void main(String[] args)  {
      Vertx vertx = Vertx.vertx();

      vertx.executeBlocking(promise -> {
        long sum = IntStream.range(0, 10000000).sum();
        promise.complete(sum);
      }, res -> {
        System.out.println("Sum: " + res.result());
      });



  }

  public static void checkthreads1() throws InterruptedException {
    System.out.println(Thread.activeCount());
    VertxOptions options=new VertxOptions();

    Vertx vertx = Vertx.vertx();
    Vertx vertx1=Vertx.vertx();

    System.out.println(Thread.activeCount());
    final Map<String, AtomicInteger> threadCounts = new ConcurrentHashMap<>();

    int verticles = 100;
    final CountDownLatch latch = new CountDownLatch(verticles);
    for (int i = 0; i < verticles; i++) {
//      vertx.deployVerticle(new MyVerticle(threadCounts), c -> latch.countDown());
//      vertx1.deployVerticle(new MyVerticle(threadCounts),c->latch.countDown());
    }
    System.out.println(Thread.activeCount());
    latch.await();
  }

    private static void deployMyVerticle2(final Vertx vertx, final Map<String, AtomicInteger> threadCounts, final AtomicInteger counter, final int verticles) {
      vertx.deployVerticle(new MyVerticle(threadCounts), c -> {
        if (counter.incrementAndGet() < verticles) {
          deployMyVerticle2(vertx, threadCounts, counter, verticles);
        }
      });
    }
}
