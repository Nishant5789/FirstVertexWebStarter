  package EventBusCode;

  import io.vertx.core.Vertx;

  public class Eventbuswithblocking {
    public static void main(String[] args) {
      Vertx vertx = Vertx.vertx();
  //    vertx.eventBus().consumer("blocking.task", msg -> {
  //      System.out.println("before block " + Thread.currentThread().getName());
  //      try {
  //        Thread.sleep(5000);
  //        System.out.println("after unblock " + Thread.currentThread().getName());
  //      } catch (InterruptedException e) {
  //        e.printStackTrace();
  //      }
  //      System.out.println("Task Completed: " + msg.body());
  //    });
  //
  //      vertx.eventBus().send("blocking.task", "Work 1");
  //      vertx.eventBus().send("blocking.task", "Work 2");
      vertx.eventBus().consumer("local.queue", msg -> {
        System.out.println("Processed: " + msg.body());
      });
      vertx.setTimer(1000, id -> {
        vertx.eventBus().send("local.queue", "Job A");
      });
      vertx.setTimer(1000, id->{
        vertx.eventBus().send("local.queue", "Job B");
      });
      vertx.setTimer(1000, id->{
        vertx.eventBus().send("local.queue", "Job C");
      });
    }
  }
