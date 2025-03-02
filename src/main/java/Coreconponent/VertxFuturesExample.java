package Coreconponent;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;

public class VertxFuturesExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    // Example: Composing Futures for file operations
    FileSystem fs = vertx.fileSystem();
    Future<FileProps> future = fs.props("src/main/java/com/Motadata/Vertexfirstexample/Coreconponent/sample.txt");

    future.onComplete(ar->{
      if(ar.succeeded()){
        FileProps props = ar.result();
        System.out.println(props.size());
      }
      else{
        System.out.println(ar.cause().getMessage());
      }
  });

    Promise<String> promise = Promise.promise();

    promise.future().onSuccess(result -> {
      System.out.println(result);
    });

    promise.complete("Hello, World!");

    Future<Void> fileOperations = fs
      .createFile("example.txt") // Step 1: Create file
      .compose(v -> {
        // Step 2: Write to the file
        return fs.writeFile("example.txt", Buffer.buffer("Hello, Vert.x!"));
      })
      .compose(v -> {
        // Step 3: Move the file
        return fs.move("example.txt", "new_example.txt");
      });

    fileOperations.onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("File operations completed successfully.");
      } else {
        System.err.println("File operations failed: " + ar.cause().getMessage());
      }
    });

    // Example: Coordinating multiple futures
    Future<Void> httpServerFuture = startHttpServer(vertx);
    Future<Void> netServerFuture = startNetServer(vertx);

    Future.all(httpServerFuture, netServerFuture).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("Both servers started successfully.");
      } else {
        System.err.println("Failed to start servers: " + ar.cause().getMessage());
      }
    });

//     Example: Interoperability with CompletionStage
    Future<String> dnsLookup = vertx.createDnsClient().lookup("vertx.io");
    dnsLookup.toCompletionStage().whenComplete((ip, err) -> {
      if (err != null) {
        System.err.println("DNS Lookup failed: " + err.getMessage());
      } else {
        System.out.println("DNS Lookup succeeded: vertx.io -> " + ip);
      }
    });

    // Keeping Vert.x running
    vertx.setTimer(5000, id -> vertx.close());
  }

//   Simulating an HTTP server start
  private static Future<Void> startHttpServer(Vertx vertx) {
    Promise<Void> promise = Promise.promise();
    vertx.createHttpServer().requestHandler(req -> req.response().end("Hello")).listen(8080, ar -> {
      if (ar.succeeded()) {
        promise.complete();
      } else {
        promise.fail(ar.cause());
      }
    });
    return promise.future();
  }

//   Simulating a Net server start
  private static Future<Void> startNetServer(Vertx vertx) {
    Promise<Void> promise = Promise.promise();
    vertx.createNetServer().connectHandler(socket -> socket.write("Welcome!"))
      .listen(1234, ar -> {
      if (ar.succeeded()) {
        promise.complete();
      } else {
        promise.fail(ar.cause());
      }
    });
    return promise.future();
  }
}

