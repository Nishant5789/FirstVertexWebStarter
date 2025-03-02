package MCQPratice.Promise_timer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class ConcurrentCheckVerticle extends AbstractVerticle {

  private Future<String> checkDeviceAvailability(String i) {
    return Future.future(promise -> {
      vertx.setTimer(5000, id -> {
        boolean isAvailable = true; // Always true for this example
        if (isAvailable) {
          promise.complete("promise : " + i);
        } else {
          promise.fail("Device unreachable");
        }
      });
    });
  }

  @Override
  public void start() {
    for (int i = 0; i < 6; i++) {
      checkDeviceAvailability(String.valueOf(i))
        .onComplete(asyncResult -> {
          if (asyncResult.succeeded()) {
            System.out.println("success : " + asyncResult.result());
          } else {
            System.out.println(asyncResult.cause());
          }
        });
    }
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ConcurrentCheckVerticle());
  }
}
