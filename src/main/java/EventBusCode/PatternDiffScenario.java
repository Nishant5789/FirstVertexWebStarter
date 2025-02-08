package EventBusCode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class PatternDiffScenario {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new EventBusVerticle());
  }
}

class EventBusVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
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
    consumer3.handler(message -> {
      System.out.println("Received message: " + message.body());
      message.reply("Message received by consumer3");
    });

    eventBus.request("indvseng", "milestone achive").
      onComplete(ar->{
        if(ar.succeeded()){
          System.out.println(ar.result().body());
        }
      });
    eventBus.request("indvseng", "wicket is taken").
      onComplete(ar->{
        if(ar.succeeded()){
          System.out.println(ar.result().body());
        }
      });
    eventBus.request("indvseng", "century miss ").
      onComplete(ar->{
        if(ar.succeeded()){
          System.out.println(ar.result().body());
        }
      });
    eventBus.request("indvseng", "catch drop").
      onComplete(ar->{
        if(ar.succeeded()){
          System.out.println(ar.result().body());
        }
      });
        eventBus.publish("indvseng", "milestone achive");
  }
  @Override
  public void stop() throws Exception {
    super.stop();
  }
}
