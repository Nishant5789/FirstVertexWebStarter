package Reassesment1;

import Reassesment1.Future.v1;
import Reassesment1.Future.v2;
import io.vertx.core.*;

public class Vertexdeploy {
  public static void main(String[] args) {
    Vertx vertx1 = Vertx.vertx();

    vertx1.deployVerticle(new v1());
    vertx1.deployVerticle(new v2(),new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));
//
//    vertx1.deployVerticle(new AbstractVerticle() {
//      @Override
//      public void start() throws Exception {
//        vertx.setPeriodic(1000,id ->
//        {
//          System.out.println(Thread.currentThread().getName());
//        });
//      }
//    }, new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));

//      vertx1.deployVerticle(new AbstractVerticle() {
//        @Override
//        public void start() {
////          vertx1.setPeriodic(1000,id-> );
//          for (int i = 0; i <3 ; i++) {
//            System.out.println(Thread.currentThread().getName());
//          }
//        }
//      },new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));
  }
}
