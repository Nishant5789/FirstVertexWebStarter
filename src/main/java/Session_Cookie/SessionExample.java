package Session_Cookie;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

public class SessionExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);

    // Create a local session store
    SessionStore store = LocalSessionStore.create(vertx);

    // Add session handler
    router.route().handler(SessionHandler.create(store));

    // Handler to set session data
    router.get("/set").handler(ctx -> {
      ctx.session().put("username", "nishant");
      ctx.response().end("Session Set: username=nishant");
    });

    // Handler to get session data
    router.get("/get").handler(ctx -> {
      String username = ctx.session().get("username");
      ctx.response().end("Session Data: " + (username != null ? username : "No session"));
    });

    // Handler to destroy session
    router.get("/destroy").handler(ctx -> {
      ctx.session().destroy();
      ctx.response().end("Session Destroyed");
    });

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}

