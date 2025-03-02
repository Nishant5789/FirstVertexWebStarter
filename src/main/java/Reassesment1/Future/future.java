package Reassesment1.Future;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public class future {
  public static void main(String[] args) {
    process().onComplete(ar->{
      if(ar.succeeded()){
        System.out.println(ar.result());
      }
      else{
        System.out.println(ar.result());
      }
    });
  }

  public static Future<String> process() {
    Promise promise = Promise.promise();
    try{
     promise.complete("complete");
     throw new RuntimeException();
    }
    catch (Exception e){
//      promise.fail("failed");
    }
    return promise.future();
  }
}
