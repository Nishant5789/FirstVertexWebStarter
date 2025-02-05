package HTTPServerClientCode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

public class HttpServerExample2 extends AbstractVerticle {
  @Override
  public void start() {
    HttpServer server = vertx.createHttpServer();

    server.requestHandler(request -> {
      HttpServerResponse response = request.response();
      // Setting status code and message
      response.setStatusCode(200).setStatusMessage("OK");
      // Setting headers
      response.putHeader("content-type", "text/html");
      // Handling different paths
      if (request.path().equals("/")) {
        response.end("Welcome to the Vert.x HTTP Server!");
      }
      else if (request.path().equals("/chunked")) {
        response.setChunked(true);
        response.write("Chunk 1\n");
        response.write("Chunk 2\n");
        response.end("End of Chunked Response");
      }
      else if (request.path().equals("/sendfile")) {
        response.sendFile("src/main/java/WEB/index.html");
      }
      else if (request.path().equals("/echo") && request.method() == HttpMethod.PUT) {
        response.setChunked(true);
        request.pipeTo(response);
      }
      else if (request.path().equals("/custom-frame")) {
        int frameType = 40;
        int frameStatus = 10;
        Buffer payload = Buffer.buffer("Custom Frame Data");
        response.writeCustomFrame(frameType, frameStatus, payload);
        response.end();
      }
      else if (request.path().equals("/reset")) {
        response.reset(8);
      }
      else if (request.path().equals("/push")) {
        response.push(HttpMethod.GET, "/main.js").onComplete(ar -> {
          if (ar.succeeded()) {
            HttpServerResponse pushedResponse = ar.result();
            pushedResponse.end("Pushed content for main.js");
          }
        });
        response.end("HTTP/2 Push initiated");
      }
      else {
        response.setStatusCode(404).end("Not Found");
      }
    });

    server.listen(8080, res -> {
      if (res.succeeded()) {
        System.out.println("Server started on port 8080");
      } else {
        System.out.println("Failed to start server: " + res.cause());
      }
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new HttpServerExample2());
  }
}

