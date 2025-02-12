package SockJsCode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class EventBusBridgeExample extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {

    Router router = Router.router(vertx);

    // Allow messages to "demo.someService" from client
    PermittedOptions inboundPermitted = new PermittedOptions().setAddress("demo.someService");

    // Allow messages from the server to the client
    PermittedOptions outboundPermitted = new PermittedOptions().setAddress("client.notifications");

    // Create SockJS handler
    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    SockJSBridgeOptions options = new SockJSBridgeOptions()
      .addInboundPermitted(inboundPermitted)
      .addOutboundPermitted(outboundPermitted)
      .setPingTimeout(5000); // If no ping received within 5 sec, SOCKET_IDLE triggers

    // Handle bridge events (intercept and modify behavior)
    router.route("/eventbus/*").subRouter(sockJSHandler.bridge(options, be -> {
      handleBridgeEvent(be);
    }));

    // Create an HTTP server and bind it to port 8080
    vertx.createHttpServer().requestHandler(router).listen(8080, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("Server started on port 8080");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private void handleBridgeEvent(BridgeEvent be) {
    BridgeEventType eventType = be.type();
    JsonObject rawMessage = be.getRawMessage();

    switch (eventType) {
      case SOCKET_CREATED:
        System.out.println("New SockJS socket created");
        break;

      case SOCKET_IDLE:
        System.out.println("Socket is idle for too long!");
        break;

      case SEND:
      case PUBLISH:
      case RECEIVE:
        if (rawMessage != null && "armadillos".equals(rawMessage.getString("body"))) {
          System.out.println("Blocked message containing 'armadillos'");
          be.complete(false); // Reject message
          return;
        }
        break;

      case REGISTER:
        System.out.println("Client registered a handler for: " + rawMessage.getString("address"));
        break;

      case UNREGISTER:
        System.out.println("Client unregistered a handler");
        break;

      case SOCKET_CLOSED:
        System.out.println("SockJS socket closed");
        break;

      default:
        break;
    }
    be.complete(true); // Allow event processing
  }
}
