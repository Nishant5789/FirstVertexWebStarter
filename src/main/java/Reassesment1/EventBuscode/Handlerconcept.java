package Reassesment1.EventBuscode;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class Handlerconcept {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    EventBus eventBus = vertx.eventBus();
    try{
      vertx.setTimer(3000,id->{
        throw new IOException("nish");
      });
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }

//    try {
//    eventBus.consumer("my.address", message -> {
//        JsonObject body = (JsonObject) message.body();
////        try{
//          if (body.getString("status").equals("fail")) {
//            throw new RuntimeException("Something went wrong!");
//          }
//          message.reply("Success: " + body);
////        }
////        catch (Exception e){
////          System.out.println(e.getMessage());
////        }
//      });
//    }
//    catch (Exception e) {
//      System.err.println("Error handling message: " + e.getMessage());
//    }
//
//// Sending messages
//    eventBus.request("my.address", new JsonObject(), reply -> {
//      if (reply.succeeded()) {
//        System.out.println("Reply: " + reply.result().body());
//      } else {
//        System.err.println("Failed: " + reply.cause().getMessage());
//      }
//    });

  }
}
