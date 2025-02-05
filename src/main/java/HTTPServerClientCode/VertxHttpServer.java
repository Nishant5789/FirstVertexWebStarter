package HTTPServerClientCode;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.MultiMap;

public class VertxHttpServer {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    HttpServer server = vertx.createHttpServer();

    server.requestHandler(request -> {
      // Print HTTP method and URI
      System.out.println("Method: " + request.method());
      System.out.println("URI: " + request.uri());

      // Print request path
      System.out.println("Path: " + request.path());

      // Print request headers
      MultiMap headers = request.headers();
      System.out.println("Headers:");
      headers.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

      // Handle the request body
      request.bodyHandler(body -> {
        System.out.println("Received body, length = " + body.length());
        // Respond with the "Hello, world!" message
        request.response().end("Hello, world!");
      });
    });

    // Start the server and listen on port 8080
    server.listen(8080, "localhost", res -> {
      if (res.succeeded()) {
        System.out.println("Server is now listening on port 8080!");
      } else {
        System.out.println("Failed to start server: " + res.cause());
      }
    });
  }
}
