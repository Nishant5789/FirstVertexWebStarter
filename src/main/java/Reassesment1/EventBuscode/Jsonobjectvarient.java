package Reassesment1.EventBuscode;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.*;

public class Jsonobjectvarient {
  public static void main(String[] args) {
    EventBus eventBus = Vertx.vertx().eventBus();

    eventBus.consumer("nish",handler->{
      try {
        throw new RuntimeException("Error in consumer");
      } catch (Exception e) {
        handler.fail(500, e.getMessage());
      }

//      JsonObject json = (JsonObject) handler.body();
//      if(handler.body().equals(true)){
//        System.out.println(handler.body());
//        throw new RuntimeException();
//      }
      handler.reply("success");
    });
    HashMap<String, String> hashMap = new HashMap<>();
    HashSet<Integer> hashSet = new HashSet<>();
    LinkedList<Integer> linkedList = new LinkedList<>();
    linkedList.add(3);
    hashSet.add(1);
    hashMap.put("key","value");
    eventBus.request("nish",true , ar->{
      if(ar.succeeded()){
        System.out.println("reply form server "+ ar.result().body());
//        System.out.println("hello   "+ar.result().body());
      }
    });
  }
}
