package Reassesment1.EventBuscode;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class localconsumer {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<JsonObject> consumer1 = eventBus.consumer("indvseng");
    MessageConsumer<JsonObject> consumer2 = eventBus.consumer("indvseng");
    MessageConsumer<JsonObject> consumer3 = eventBus.consumer("indvseng");


    consumer1.handler(message -> {
      System.out.println("Received message: " + message.body());
      message.reply("Message received by consumer1");
    });
    consumer2.handler(message -> {
      System.out.println("Received message: " + message.body());
      message.reply("Message received by consumer2");
    });

    eventBus.publish("indvseng", "milestone achive");

    consumer3.handler(message -> {
      System.out.println("Received message: " + message.body());
      message.reply("Message received by consumer3");
    });


  }
}
