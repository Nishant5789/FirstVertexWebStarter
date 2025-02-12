package EventBusCode;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class Eventbuswithworkerblocking extends AbstractVerticle {
  @Override
  public void start() {
    vertx.eventBus().consumer("work.queue", msg -> {
      vertx.executeBlocking(promise -> {
        try {
          Thread.sleep(2000);
          promise.complete("Processed: " + msg.body());
        } catch (InterruptedException e) {
          promise.fail(e);
        }
      }).onComplete(res -> {
        if (res.succeeded()) {
          System.out.println(res.result());
        }
      });
    });

    vertx.setTimer(1000, id -> {
      vertx.eventBus().send("work.queue", "Task 1");
      vertx.eventBus().send("work.queue", "Task 2");
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Eventbuswithworkerblocking());
  }
}
