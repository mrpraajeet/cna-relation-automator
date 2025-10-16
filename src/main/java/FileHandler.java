import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class FileHandler {
  public static Set<String> readUidsFromFile() {
    Set<String> uids = new LinkedHashSet<>();

    try (InputStream stream = FileHandler.class.getResourceAsStream(Config.UIDS_FILE)) {
      if (stream == null) {
        System.err.printf("Could not find resource file: %s%n", Config.UIDS_FILE);
        return uids;
      }

      try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8)) {
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine().trim();
          if (!line.isEmpty()) {
            uids.add(line);
          }
        }
      }
    } catch (IOException e) {
      System.err.printf("Error reading from file: %s%n", e.getMessage());
    }

    return uids;
  }

  public static void writeUidsToFile(Set<String> uids) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(Config.UIDS_FILE, false))) {
      for (String uid : uids) {
        writer.write(uid);
        writer.newLine();
      }
      IO.println("Successfully wrote %d UIDs to %s%n".formatted(uids.size(), Config.UIDS_FILE));
    } catch (IOException e) {
      System.err.printf("Error writing to file: %s%n", e.getMessage());
    }
  }
}
