package Coreconponent;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class EventBusExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
      vertx.deployVerticle(new EventBusExample());
  }

  @Override
  public void start() {
    EventBus eventBus = vertx.eventBus();
    // Registering a message consumer with a handler
    MessageConsumer<JsonObject> consumer = eventBus.consumer("news.uk.sport");
    consumer.handler(message -> {
      System.out.println("Received message: " + message.body());
      // Replying back to the sender
      message.reply(new JsonObject().put("reply", "Message received and processed"));
    });

//     Completion handler when the consumer is registered
    consumer.completionHandler(res -> {
      if (res.succeeded()) {
        System.out.println("Handler registered successfully");
      } else {
        System.out.println("Handler registration failed!");
      }
    });

    // Sending a message with DeliveryOptions (Headers + Timeout)
    DeliveryOptions options = new DeliveryOptions()
      .addHeader("priority", "high")
      .setSendTimeout(5000); // 5 seconds timeout

    eventBus.request("news.uk.sport", new JsonObject().put("headline", "Breaking News!"))
      .onComplete(ar->{
        if(ar.succeeded()){
          System.out.println("recevied" + ar.result().body());
        }
      });

//    eventBus.request("news.uk.sport", new JsonObject().put("headline", "Breaking News!"), options)
//      .onComplete(ar -> {
//        if (ar.succeeded()) {
//          System.out.println("Received reply: " + ar.result().body());
//        } else {
//          System.out.println("Failed to get a reply: " + ar.cause().getMessage());
//        }
//      });

    // Publishing a message (Multiple handlers receive it)
//    eventBus.publish("news.uk.sport", new JsonObject().put("headline", "Live Update!"));

    // Unregistering the consumer after 10 seconds
//    vertx.setTimer(10000, id -> {
//      consumer.unregister().onComplete(res -> {
//        if (res.succeeded()) {
//          System.out.println("Handler unregistered successfully");
//        } else {
//          System.out.println("Handler unregistration failed!");
//        }
//      });
//    });
  }
}
