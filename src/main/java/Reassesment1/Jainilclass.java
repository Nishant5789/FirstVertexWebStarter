package Reassesment1;

import io.vertx.core.*;

public class Jainilclass extends AbstractVerticle {
    public static Future<Void> run()
    {
        Promise promise= Promise.<Void>promise();
        promise.complete();
        try
        {
            throw new RuntimeException();
        }
        catch (Exception e)
        {
            promise.fail(e);
        }
        return promise.future();
    }

//    public static void main(String[] args) {
//        run().onComplete(result->{
//            System.out.println(result.succeeded());
//        });
//    }

  @Override
  public void start()
  {
    System.out.println(Thread.currentThread().getName()+" started l");
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(2));

    vertx.deployVerticle(Jainilclass.class.getName(),new DeploymentOptions()
      .setInstances(5).setThreadingModel(ThreadingModel.WORKER)
      .setWorkerPoolSize(5).setWorkerPoolName("workerpool"))
      .onComplete(result -> {
      System.out.println(Thread.currentThread().getName()+" deployed l");
    });
  }

}
