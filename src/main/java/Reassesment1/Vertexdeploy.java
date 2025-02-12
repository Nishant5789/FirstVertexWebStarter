package Reassesment1;

import io.vertx.core.*;

public class Vertexdeploy {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(3));
//    for (int i = 0; i < 3; i++) {
//      vertx.deployVerticle(new AbstractVerticle() {
//        @Override
//        public void start() {
//          String threadName = Thread.currentThread().getName();
//          Vertx.vertx().setPeriodic(1000,id-> System.out.println(threadName));
//          System.out.println(threadName);
//        }
//      }, new DeploymentOptions().setInstances(2).setThreadingModel(ThreadingModel.WORKER));
//    }
  }

}
