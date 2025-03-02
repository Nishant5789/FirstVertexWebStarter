package EventBusCode;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;

public class EventBuswithtimeoutmechnism {
  public static void main(String[] args) {
    Evenbusblockingtest1();
//    Vertx vertx = Vertx.vertx();
//    // Deploy the receiver verticle
//    vertx.deployVerticle(new MessageReceiver(), new DeploymentOptions().setThreadingModel(ThreadingModel.EVENT_LOOP));
////    // Deploy the sender verticle after a delay
//    vertx.setTimer(2000, id -> vertx.deployVerticle(new MessageSender()));
  }

  private static void Evenbusblockingtest1() {
      Vertx vertx = Vertx.vertx();

      vertx.eventBus().consumer("blocking.task", msg -> {
        System.out.println("Before Block " + Thread.currentThread().getName());
        try {
          Thread.sleep(5000);
          System.out.println("After Block " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("Task Completed: " + msg.body());
      });

      vertx.eventBus().send("blocking.task", "Work 1",new DeliveryOptions().setSendTimeout(3000));
      vertx.eventBus().send("blocking.task", "Work 2");
    }
}

class MessageReceiver extends AbstractVerticle {
  @Override
  public void start() {
    vertx.eventBus().consumer("event.virtualaddress", message -> {
      if("fail".equals(message.body())){
        throw new RuntimeException("forced failure");
      }
//      Vertx.vertx().setTimer(4000,id->message.reply("reply from server"));
      try {
        Thread.sleep(4000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      message.reply("reply from server");
      System.out.println("continue processig");
      message.reply("reply from server");
    });

    vertx.eventBus().consumer("event.address", message -> {
      System.out.println("📩 Received: " + message.body() + " " + Thread.currentThread().getName());

      if ("fail".equals(message.body())) {
//        Failure Code	Meaning
//        0	Generic failure (default) 1	Processing error  2	Timeout occurred  3	Invalid request format
//          4	Unauthorized access  5	Resource not found 6	Internal server error 7	Database error
//          8	Dependency failure  9	Rate limit exceeded 10	Service unavailable
        message.fail(1, "Processing error");  // 🚨 Simulating failure
      }
      else {
        vertx.setTimer(800, id -> {
          message.replyAndRequest("Reply from server", reply -> {
            if (reply.succeeded()) {
              System.out.println("✅ Reply received: " + reply.result().body());
            } else {
              System.out.println("❌ Reply failed: " + reply.cause().getMessage());
            }
          });
        });
      }
    });
  }
}

// 📌 Sender (Sends messages and handles replies with timeout)
class MessageSender extends AbstractVerticle {
  @Override
  public void start() {
    vertx.eventBus().request("event.virtualaddress","pass",
      new DeliveryOptions().setSendTimeout(3000),reply->{
        if(reply.succeeded()){
          System.out.println("server responce " + reply.result().body());
        }
        else{
          System.out.println("⏳ Request timed out or failed: " + reply.cause().getMessage());
        }
      });
//    vertx.eventBus().request("event.address", "Hello from sender",//fail
//      new DeliveryOptions().setTracingPolicy(TracingPolicy.ALWAYS).setSendTimeout(1000),
//      reply -> {
//      if (reply.succeeded()) {
//        System.out.println("✔ Server Response: " + reply.result().body());
//
//        // Getting the reply address
//        String replyAddress = reply.result().replyAddress();
//        System.out.println("📬 Reply Address: " + replyAddress);
//
//        // Sending another request to the reply address with timeout
//        vertx.eventBus().request(replyAddress, "Follow-up Message",
//          new DeliveryOptions().setSendTimeout(1000),
//          followUp -> {
//          if (followUp.succeeded()) {
//            System.out.println("✔ Follow-up Response: " + followUp.result().body());
//          } else {
//            System.out.println("❌ Follow-up failed: " + followUp.cause().getMessage());
//          }
//        });
//      } else {
//        System.out.println("⏳ Request timed out or failed: " + reply.cause().getMessage());
//      }
//    });
  }
}
