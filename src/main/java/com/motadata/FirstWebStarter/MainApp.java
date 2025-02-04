package com.motadata.FirstWebStarter;

import io.vertx.core.Vertx;

public class MainApp{
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new MainVerticle(), res->{
      if(res.succeeded()) {
        System.out.println("verticle deployed succesfully  Deployment ID: " + res.result());
      }
      else{
        System.out.println("Failed to deploy Verticle: " + res.cause().getMessage());
      }
    });
  }
}
