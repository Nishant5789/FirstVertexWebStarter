package EventBusCode;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

class MyPOJO {
  private String orderId;
  private double amount;

  public MyPOJO(String orderId, double amount) {
    this.orderId = orderId;
    this.amount = amount;
  }
  public String getOrderId() {
    return orderId;
  }
  public double getAmount() {
    return amount;
  }
  public JsonObject toJson() {
    return new JsonObject().put("orderId", orderId).put("amount", amount);
  }
  public static MyPOJO fromJson(JsonObject json) {
    return new MyPOJO(json.getString("orderId"), json.getDouble("amount"));
  }
  @Override
  public String toString() {
    return "Order ID: " + orderId + ", Amount: " + amount;
  }
}

class MyPOJOCodec implements MessageCodec<MyPOJO, MyPOJO> {
  @Override
  public void encodeToWire(Buffer buffer, MyPOJO myPOJO) {
    JsonObject json = myPOJO.toJson();
    byte[] bytes = json.encode().getBytes();
    buffer.appendInt(bytes.length);
    buffer.appendBytes(bytes);
  }
  @Override
  public MyPOJO decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);
    String jsonStr = buffer.getString(pos + 4, pos + 4 + length);
    return MyPOJO.fromJson(new JsonObject(jsonStr));
  }
  @Override
  public MyPOJO transform(MyPOJO myPOJO) {
    return myPOJO;  // Pass-through for local delivery
  }
  @Override
  public String name() {
    return "myPojoCodec";
  }
  @Override
  public byte systemCodecID() {
    return -1;  // User-defined codec
  }
}

public class MessageCodecExample extends AbstractVerticle {
  @Override
  public void start() {
    Vertx c = Vertx.vertx();
    EventBus eventBus = vertx.eventBus();
    MyPOJOCodec myCodec = new MyPOJOCodec();
    // Register custom codec
//    eventBus.registerCodec(myCodec);
    // Register default codec for MyPOJO class
    eventBus.registerDefaultCodec(MyPOJO.class, myCodec);
//    vertx.setTimer(100,id->vertx.deployVerticle(new ReceiverVerticle()));
    vertx.deployVerticle(new ReceiverVerticle());

    // Send message using the codec
    DeliveryOptions options = new DeliveryOptions().setCodecName(myCodec.name());
    MyPOJO order = new MyPOJO("12345", 599.99);

    vertx.setTimer(1000, id -> {
      System.out.println("Sending order...");
      eventBus.send("orders", order, options);
    });

    // Unregister codec after 5 seconds
    vertx.setTimer(5000, id -> {
      eventBus.unregisterCodec(myCodec.name());
      System.out.println("Codec unregistered.");
    });
  }

  static class ReceiverVerticle extends AbstractVerticle {
    @Override
    public void start() {
      EventBus eventBus = vertx.eventBus();
      eventBus.consumer("orders", message -> {
        MyPOJO order = (MyPOJO) message.body();
        System.out.println("Received order: " + order);
      });
    }
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MessageCodecExample());
  }
}
