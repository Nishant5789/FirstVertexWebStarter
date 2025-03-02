package JsonHandling;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.DecodeException;

public class JsonMethodsVerticle extends AbstractVerticle {

  // Sample Java class to map JSON to/from
  public static class SystemStats {
    private String systemOverallMemoryFreeBytes;
    private String systemMemoryUsedPercent;
    private String systemCpuCores;
    private String systemName;
    private String startedTime;

    // Default constructor (required for mapFrom)
    public SystemStats() {}

    // Getters and setters
    public String getSystemOverallMemoryFreeBytes() { return systemOverallMemoryFreeBytes; }
    public void setSystemOverallMemoryFreeBytes(String value) { this.systemOverallMemoryFreeBytes = value; }
    public String getSystemMemoryUsedPercent() { return systemMemoryUsedPercent; }
    public void setSystemMemoryUsedPercent(String value) { this.systemMemoryUsedPercent = value; }
    public String getSystemCpuCores() { return systemCpuCores; }
    public void setSystemCpuCores(String value) { this.systemCpuCores = value; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String value) { this.systemName = value; }
    public String getStartedTime() { return startedTime; }
    public void setStartedTime(String value) { this.startedTime = value; }
  }

  @Override
  public void start() {
    // 1. JSON String to JsonObject
    String jsonString = "{\"systemOverallMemoryFreeBytes\": \"12345678\", " +
      "\"systemMemoryUsedPercent\": \"45.5\", " +
      "\"systemCpuCores\": \"8\", " +
      "\"systemName\": \"Server1\", " +
      "\"startedTime\": \"2025-03-02 10:00:00\"}";

    try {
      JsonObject jsonObject = new JsonObject(jsonString);
      System.out.println("1. JSON String to JsonObject: " + jsonObject.encodePrettily());

      // 2. JsonObject to Java Object (mapFrom)
      SystemStats stats = JsonObject.mapFrom(jsonObject).mapTo(SystemStats.class);
      System.out.println("2. JsonObject to SystemStats object: " +
        "systemName=" + stats.getSystemName() + ", " +
        "cpuCores=" + stats.getSystemCpuCores());

      // 3. Java Object to JsonObject (mapTo)
      JsonObject jsonFromObject = JsonObject.mapFrom(stats);
      System.out.println("3. SystemStats object to JsonObject: " + jsonFromObject.encodePrettily());

      // 4. Useful JsonObject Methods
      // Get specific value
      String systemName = jsonObject.getString("systemName");
      System.out.println("4a. Get systemName: " + systemName);

      // Put new key-value pair
      jsonObject.put("systemOsName", "Linux");
      System.out.println("4b. After put (systemOsName): " + jsonObject.encode());

      // Merge another JsonObject
      JsonObject extraData = new JsonObject()
        .put("systemOsVersion", "5.15")
        .put("systemName", "ServerUpdated"); // Overwrites existing key
      jsonObject.mergeIn(extraData);
      System.out.println("4c. After mergeIn: " + jsonObject.encodePrettily());

      // Check if key exists
      boolean hasCpuCores = jsonObject.containsKey("systemCpuCores");
      System.out.println("4d. Contains systemCpuCores: " + hasCpuCores);

      // Remove a key
      jsonObject.remove("systemOsVersion");
      System.out.println("4e. After remove (systemOsVersion): " + jsonObject.encode());

      // Iterate over fields
      System.out.println("4f. Iterating fields:");
      jsonObject.forEach(entry -> {
        System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
      });

      // Encode to string (compact and pretty)
      String compactJson = jsonObject.encode();
      String prettyJson = jsonObject.encodePrettily();
      System.out.println("4g. Compact JSON: " + compactJson);
      System.out.println("4h. Pretty JSON: " + prettyJson);

      // Copy JsonObject
      JsonObject copy = jsonObject.copy();
      System.out.println("4i. Copied JsonObject: " + copy.encode());

      // Clear JsonObject
      jsonObject.clear();
      System.out.println("4j. After clear: " + jsonObject.encode());

    } catch (DecodeException e) {
      System.err.println("Failed to parse JSON string: " + e.getMessage());
    }
  }

  // Example method integrating with Future (e.g., save to DB)
  public Future<Void> saveJson(String jsonString) {
    try {
      JsonObject payload = new JsonObject(jsonString); // JSON String to JsonObject
      SystemStats stats = payload.mapTo(SystemStats.class); // JsonObject to Object
      JsonObject jsonFromStats = JsonObject.mapFrom(stats); // Object to JsonObject
      System.out.println("Processed JSON: " + jsonFromStats.encodePrettily());
      return Future.succeededFuture();
    } catch (Exception e) {
      return Future.failedFuture("Failed to process JSON: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new JsonMethodsVerticle());
  }
}
