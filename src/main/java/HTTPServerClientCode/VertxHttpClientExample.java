package HTTPServerClientCode;

import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.Future;

public class VertxHttpClientExample {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    // 1. Creating an HTTP Client
    // Default client
//    HttpClient defaultClient = vertx.createHttpClient();
//
//    // Client with custom options
//    HttpClientOptions options = new HttpClientOptions()
//      .setKeepAlive(false);
    // Example for disabling keep-alive
//    HttpClient clientWithOptions = vertx.createHttpClient(options);

    // 2. HTTP/2 Support
    // HTTP/2 over TLS (h2)
//    HttpClientOptions http2Options = new HttpClientOptions()
//      .setProtocolVersion(HttpVersion.HTTP_2)
//      .setSsl(true)
//      .setUseAlpn(true)
//      .setTrustAll(true);
//
//    HttpClient http2Client = vertx.createHttpClient(http2Options);

    // HTTP/2 over TCP (h2c)
//    HttpClientOptions http2cOptions = new HttpClientOptions()
//      .setProtocolVersion(HttpVersion.HTTP_2);
//
//    HttpClient http2cClient = vertx.createHttpClient(http2cOptions);
//
//     3. Connection Pooling
//    PoolOptions poolOptions = new PoolOptions().setHttp1MaxSize(10);

    // 4. Making Requests (GET request example)
    HttpClient clientWithOptions = vertx.createHttpClient();
    clientWithOptions
      .request(HttpMethod.GET, 8080, "localhost", "/")
      .onComplete(ar1 -> {
        if (ar1.succeeded()) {
          HttpClientRequest request = ar1.result();
          request.send().onComplete(ar2 -> {
            if (ar2.succeeded()) {
              System.out.println("Response Status: " + ar2.result().statusMessage());
            }
          });
        }
      });

    // 5. Writing Headers
//    HttpHeaders.headers("content-type", "application/json")
    MultiMap headers = null;
    clientWithOptions.request(HttpMethod.GET, 8080, "myserver.com", "/some-uri")
      .onComplete(ar1 -> {
        if (ar1.succeeded()) {
          HttpClientRequest request = ar1.result();
          request.headers().addAll(headers);
        }
      });

    // 6. Sending Requests with Body (String, Buffer, Stream)
//    HttpClient clientWithOptions = vertx.createHttpClient();
//    clientWithOptions.request(HttpMethod.POST, 8080, "localhost", "/upload")
//      .onComplete(ar1 -> {
//        if (ar1.succeeded()) {
//          HttpClientRequest request = ar1.result();
//          request.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//          // String Body
//          request.send("Hello World");
//
//          // Buffer Body
//          request.send(Buffer.buffer("Hello World"));
//
//          // Streaming Body
//          request.putHeader(HttpHeaders.CONTENT_LENGTH, "1000")
//            .send(Buffer.buffer("Stream data"));
//        }
//      });

    // 7. Writing & Ending Requests
//    clientWithOptions.request(HttpMethod.POST, 8080, "myserver.com", "/submit")
//      .onComplete(ar1 -> {
//        if (ar1.succeeded()) {
//          HttpClientRequest request = ar1.result();
//
//          // Writing Manually
//          request.write("some data");
//          request.end();
//
//          // Writing & Ending Together
//          request.end("some simple data");
//        }
//      });

    // 8. Content-Length & Chunked Encoding
//    clientWithOptions.request(HttpMethod.GET, 8080, "myserver.com", "/stream")
//      .onComplete(ar1 -> {
//        if (ar1.succeeded()) {
//          HttpClientRequest request = ar1.result();
//          // Vert.x will handle chunked encoding automatically if Content-Length is not set
//          request.end();
//        }
//      });
  }
}

