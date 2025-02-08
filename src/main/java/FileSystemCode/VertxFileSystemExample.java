package FileSystemCode;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VertxFileSystemExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    FileSystem fs = vertx.fileSystem();
    String prefixPath = "src/main/java/FileSystemCode/dummyfolder/";
    String filePath = prefixPath + "foo.txt";
    String filePath1 = prefixPath + "foo1.txt";
    String filePath2 = prefixPath + "foo2.txt";

    // 1. Copy a file (Async & Sync)
//    fs.copy(prefixPath+"foo.txt", prefixPath+"bar.txt")
//      .onComplete(res->
//      {
//        if(res.succeeded()){
//          System.out.println("file copy");
//        }
//        else{
//          System.out.println(res.cause());
//        }
//      });
//      .onComplete(res -> System.out.println(res.succeeded() ? "File copied (async)" : "Copy failed"));
//    fs.copyBlocking("foo.txt", "bar.txt");
//    System.out.println("File copied (blocking)");

//     2. Read File (Async)

    fs.readFile(filePath).onComplete(res -> {
      if (res.succeeded()) {
        System.out.println("Read file: " + res.result().toString());
      } else {
        System.out.println("Failed to read file");
      }
    });

//    // 3. Write to a File (Async)
    fs.writeFile(filePath, Buffer.buffer("Hello Vert.x"))
      .onComplete(res ->
      {
        System.out.println(res.succeeded() ? "File written" : "Write failed");
      });

    // 4. Check if File Exists
    fs.exists(filePath).onComplete(exists -> {
      if (exists.succeeded() && exists.result()) {
        System.out.println("File exists: " + filePath);
      } else {
        System.out.println("File does not exist");
      }
    });

//    fs.open()

    // 5. Open a File Asynchronously (for AsyncFile operations)
    fs.open(filePath1, new OpenOptions().setRead(true).setWrite(true))
      .onComplete(openRes -> {
        if (openRes.succeeded()) {
          AsyncFile file = openRes.result();

          // 6. Random Access Write
          file.write(Buffer.buffer("Hello AsyncFile"), 5)
            .onComplete(ar -> System.out.println("Random Access Write completed"));

          // 7. Random Access Read
//    file.read(buffer, 0, 5, 10);
//📌 This reads 10 bytes from the file, starting at byte 5, and stores the
//    data in buffer starting at index 0 of the buffer.

          Buffer buffer = Buffer.buffer(20);
          file.read(buffer, 0, 0, buffer.length())
            .onComplete(ar -> System.out.println("Read from file: " + buffer.toString()));

          // 8. Piping AsyncFile as a Stream
            fs.open(filePath2, new OpenOptions().setWrite(true)).onComplete(outputRes -> {
              if (outputRes.succeeded()) {
                AsyncFile outputFile = outputRes.result();
                file.pipeTo(outputFile).onComplete(pipeRes -> {
                  if (pipeRes.succeeded()) {
                    System.out.println(pipeRes.result());
                    System.out.println("File streaming completed.");
                  }
                  outputFile.close();
                });
              }
            });
          // 9. Closing the AsyncFile
          file.close().onComplete(ar -> System.out.println("File closed"));
        }
      });

//    // 10. Delete the file
//    fs.delete(filePath).onComplete(res -> System.out.println(res.succeeded() ? "File deleted" : "Delete failed"));
  }
}
