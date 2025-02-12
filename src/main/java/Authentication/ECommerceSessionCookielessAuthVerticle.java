package Authentication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.handler.SessionHandler;

import java.util.UUID;

  public class ECommerceSessionCookielessAuthVerticle extends AbstractVerticle {

  private SessionStore sessionStore;

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    // Enable reading request body
    router.route().handler(BodyHandler.create());

    // Initialize session store (Local storage)
    sessionStore = LocalSessionStore.create(vertx);
    // Attach session handler to store sessions
    router.route().handler(SessionHandler.create(sessionStore));

    // Public route for login
    router.get("/").handler(ctx->{
      ctx.end("server is on");
    });

    router.post("/login").handler(this::handleLogin);

    // Protect e-commerce routes with session validation
    router.route("/ecommerce/*").handler(this::validateSession);

    // Sample protected e-commerce routes
    router.get("/ecommerce/products").handler(this::getProducts);
    router.post("/ecommerce/cart/add").handler(this::addToCart);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("Server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  // Handle login: Generate Session ID and store it in the session store
  private void handleLogin(RoutingContext ctx) {
    JsonObject body = ctx.body().asJsonObject();
    String username = body.getString("username");
    String password = body.getString("password");
    // Dummy authentication check
    if ("user123".equals(username) && "password123".equals(password)) {
      Session session = sessionStore.createSession(300000); // 5-minute session expiry
      session.put("username", username); // Store user info

      // Store session and return correct session ID
      sessionStore.put(session, res -> {
        if (res.succeeded()) {
          ctx.json(new JsonObject().put("sessionId", session.id())); // Return correct session ID
        } else {
          ctx.response().setStatusCode(500).end("Failed to create session");
        }
      });
    } else {
      ctx.response().setStatusCode(401).end("Invalid credentials");
    }

  }

  // Middleware: Validate session before accessing protected routes
  private void validateSession(RoutingContext ctx) {
    String sessionId = ctx.request().getHeader("Session-ID"); // Extract session ID from headers

    if (sessionId == null) {
      ctx.response().setStatusCode(401).end("Unauthorized: Session ID missing");
      return;
    }

    sessionStore.get(sessionId, res -> {
      if (res.succeeded() && res.result() != null) {
        Session session = res.result();
        ctx.put("username", session.get("username")); // Attach user info for further processing
        ctx.next(); // Proceed to the requested route
      } else {
        ctx.response().setStatusCode(401).end("Unauthorized: Invalid Session ID");
      }
    });
  }

  // Sample protected e-commerce route: Fetch product list
  private void getProducts(RoutingContext ctx) {
    ctx.json(new JsonObject()
      .put("products", new JsonObject()
        .put("1", "Laptop")
        .put("2", "Phone")
        .put("3", "Headphones")));
  }

  // Sample protected e-commerce route: Add to cart
  private void addToCart(RoutingContext ctx) {
    ctx.json(new JsonObject().put("message", "Item added to cart successfully"));
  }

  public static void main(String[] args) {
    Vertx vert = Vertx.vertx();
    vert.deployVerticle(new ECommerceSessionCookielessAuthVerticle());
  }
}
