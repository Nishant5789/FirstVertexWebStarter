package com.motadata.FirstWebStarter;

import io.vertx.core.AbstractVerticle;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class MyVerticle extends AbstractVerticle {
  private final Map<String, AtomicInteger> threadCounts;

  MyVerticle(Map<String, AtomicInteger> threadCounts) {
    this.threadCounts = threadCounts;
  }

  @Override
  public void start() {
    threadCounts.computeIfAbsent(Thread.currentThread().getName(),
      t -> new AtomicInteger(0)).incrementAndGet();
  }
}
