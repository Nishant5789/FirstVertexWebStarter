package Coreconponent;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.net.JksOptions;

public class ClusteredEventBusExample {
  public static void main(String[] args) {
    VertxOptions options = new VertxOptions()
      .setEventBusOptions(new EventBusOptions()
        .setSsl(true)  // Enable SSL for secure communication
        .setKeyStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
        .setTrustStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
//        .setClientAuth(ClientAuth.REQUIRED)  // Require client authentication
        .setClusterPublicHost("localhost")  // Set public host for container access
        .setClusterPublicPort(1234)  // Set public port
      );

    Vertx.clusteredVertx(options).onComplete(res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        EventBus eventBus = vertx.eventBus();
        System.out.println("Clustered Event Bus started: " + eventBus);

        // Register a message consumer
        MessageConsumer<String> consumer = eventBus.consumer("news.world");
        consumer.handler(message -> {
          System.out.println("Received message: " + message.body());
          message.reply("Message processed successfully");
        });

        // Send a message with headers and timeout
        DeliveryOptions deliveryOptions = new DeliveryOptions()
          .addHeader("priority", "high")
          .setSendTimeout(5000);

        vertx.setTimer(5000, id -> {
          eventBus.request("news.world", "Breaking News!", deliveryOptions).onComplete(reply -> {
            if (reply.succeeded()) {
              System.out.println("Reply received: " + reply.result().body());
            } else {
              System.out.println("Failed to receive reply: " + reply.cause());
            }
          });
        });
      } else {
        System.out.println("Failed to start clustered event bus: " + res.cause());
      }
    });
  }
}
