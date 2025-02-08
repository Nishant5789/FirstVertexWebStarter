package SockJsCode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

public class SockJSServer extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);

    // Configure SockJS handler
    SockJSHandlerOptions options = new SockJSHandlerOptions().setRegisterWriteHandler(true);
    SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);

    router.route("/myapp/*").subRouter(sockJSHandler.socketHandler(sockJSSocket -> {
      String writeHandlerID = sockJSSocket.writeHandlerID();
      System.out.println("Client connected. WriteHandlerID: " + writeHandlerID);

      // Handle messages from the client
      sockJSSocket.handler(message -> {
        System.out.println("Received from client: " + message.toString());
        sockJSSocket.write(Buffer.buffer("Echo: " + message.toString()));
      });

      // Simulate sending data to the client asynchronously
      vertx.setPeriodic(5000, id -> {
        vertx.eventBus().send(writeHandlerID, Buffer.buffer("Periodic update from server"));
      });

      sockJSSocket.closeHandler(v -> System.out.println("Client disconnected"));
    }));

    vertx.createHttpServer().requestHandler(router).listen(8080, res -> {
      if (res.succeeded()) {
        System.out.println("Server started on port 8080");
      } else {
        System.out.println("Failed to start server");
      }
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new SockJSServer());
  }
}
