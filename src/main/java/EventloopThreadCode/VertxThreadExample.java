package EventloopThreadCode;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBusOptions;

public class VertxThreadExample {
  public static void main(String[] args) {
    System.out.println(ProcessHandle.current().pid());
    System.out.println("Before starting Vert.x -> Active Threads: " + Thread.activeCount());

    Vertx vertx = Vertx.vertx( new VertxOptions()
//        .setEventLoopPoolSize(4).setWorkerPoolSize(2)
//      .setEventBusOptions(new EventBusOptions()
//        .setClusterPingInterval(15000))
//      .setMaxEventLoopExecuteTime(2000).setMaxWorkerExecuteTime(60000).setBlockedThreadCheckInterval(1000)
//      .setFileSystemOptions()
//      .setMetricsOptions()
//      .setAddressResolverOptions()
//      .setUseDaemonThread()
//      .setWarningExceptionTime(2000)
    );

    for (int i = 0; i < 22; i++) {
          vertx.deployVerticle(new MainVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.EVENT_LOOP));
    }

//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));
//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));
//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));
//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));

//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions());
//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions());
//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions());
//    vertx.deployVerticle(new MainVerticle(), new DeploymentOptions());

    System.out.println("After starting Vert.x -> Active Threads: " + Thread.activeCount());
    vertx.close();
  }
}

class MainVerticle extends AbstractVerticle{
  @Override
  public void start() throws Exception {
    System.out.println(Thread.currentThread().getName());
    vertx.setTimer(1000,l->{
    });
  }

  @Override
  public void stop() throws Exception {
//    System.out.println("stop");
  }
}
