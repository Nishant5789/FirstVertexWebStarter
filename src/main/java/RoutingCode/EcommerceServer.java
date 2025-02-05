package RoutingCode;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class EcommerceServer extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    // 1. Basic Route
    router.route("/").handler(ctx -> ctx.response().end("Welcome to the E-commerce API"));

    // 2. Handling Requests Sequentially
    router.route("/info").handler(ctx -> {
      ctx.response().setChunked(true).write("Step 1\n");
      ctx.next();
    });

    router.route("/info").handler(ctx -> {
      ctx.response().write("Step 2\n");
      ctx.next();
    });

    router.route("/info").handler(ctx -> {
      ctx.response().write("Final Step");
      ctx.response().end();
    });

    // 3. JSON Response
    router.get("/status").respond(ctx -> Future.succeededFuture(new JsonObject().put("status", "running")));

    // 4. Blocking Handler
    router.route("/heavy-task").blockingHandler(ctx -> {
      try {
        Thread.sleep(3000);
      }
      catch (InterruptedException e) {
      }
      ctx.response().end("Heavy task completed!");
    });

    // 5. Routing with Path Parameters
    Router apiRouter = Router.router(vertx);
    apiRouter.route().handler(BodyHandler.create());

    apiRouter.get("/products/:productID").handler(this::getProduct);
    apiRouter.put("/products/:productID").handler(this::updateProduct);
    apiRouter.delete("/products/:productID").handler(this::deleteProduct);

    router.mountSubRouter("/api", apiRouter);

    // 6. Request Parameters, Query Params, URL Details, and Remote Address
    router.get("/details/:id").handler(ctx -> {
      String id = ctx.pathParam("id");
      String queryParam = ctx.queryParam("type").isEmpty() ? "N/A" : ctx.queryParam("type").get(0);
      String absoluteUrl = ctx.request().absoluteURI();
      String remoteAddr = ctx.request().remoteAddress().toString();

      JsonObject response = new JsonObject()
        .put("id", id)
        .put("queryParam", queryParam)
        .put("absoluteUrl", absoluteUrl)
        .put("remoteAddress", remoteAddr)
        .put("url", ctx.request().uri());

      ctx.response().putHeader("content-type", "application/json").end(response.encodePrettily());
    });


    // 6. MIME Type Handling
    router.post("/upload")
      .consumes("application/json")
      .handler(ctx -> ctx.response().end("JSON Uploaded!"));

    router.get("/download")
      .produces("application/json")
      .handler(ctx -> ctx.response().putHeader("content-type", "application/json").end(new JsonObject().put("message", "JSON Data").encode()));

    // 7. Rerouting & Error Handling
    router.get("/notfound").handler(ctx -> ctx.response().setStatusCode(404).end("Page Not Found"));

    router.get().failureHandler(ctx -> {
      if (ctx.statusCode() == 404) {
        ctx.reroute("/notfound");
      } else {
        ctx.next();
      }
    });

//  // 8. Start Server
    vertx.createHttpServer().requestHandler(router).listen(8080)
      .onSuccess(server -> startPromise.complete())
      .onFailure(startPromise::fail);
  }

  private void getProduct(RoutingContext ctx) {
    String productID = ctx.pathParam("productID");
    ctx.response().end("Product details for ID: " + productID);
  }

  private void updateProduct(RoutingContext ctx) {
    ctx.response().setStatusCode(200).end("Product updated");
  }

  private void deleteProduct(RoutingContext ctx) {
    ctx.response().setStatusCode(204).end("Product deleted");
  }

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new EcommerceServer());
  }
}
