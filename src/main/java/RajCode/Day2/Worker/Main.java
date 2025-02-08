package RajCode.Day2.Worker;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {

  public static void main(String[] args) {
//    System.out.println("Before starting Vert.x -> Active Threads: " + Thread.activeCount());
    Vertx vertx = Vertx.vertx();

    //    vertx.deployVerticle(new TestNormal());
//    vertx.deployVerticle(new TestNormal());
//    vertx.deployVerticle(new TestNormal());

    // best case analysis
    vertx.deployVerticle(new TestNormal());

//    System.out.println("after starting Vert.x -> Active Threads: " + Thread.activeCount());

  }
}

