
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import java.util.ArrayList;
import java.util.List;

public class Requestor {
  public Requestor(APIRequestContext request, int start, int end) {
    List<String> uids = new ArrayList<>(FileHandler.readUidsFromFile());
    IO.println("Read %d UIDs from file".formatted(uids.size()));

    if (end == Integer.MAX_VALUE) {
      end = uids.size();
    }

    if (start < 1 || end > uids.size() || start > end) {
      System.err.println("Invalid range provided. Start: " + start + ", End: " + end + ", Total UIDs: " + uids.size());
      return;
    }

    IO.println("Processing UIDs from " + start + " to " + end);

    for (int i = start - 1; i < end; i++) {
      String uid = uids.get(i);
      IO.println("Changing %s's relation towards UID: %s (%d)".formatted(Config.USERNAME, uid, i + 1));

      String url = String.format(Config.PROFILE_PATH, uid) + Config.GAME_PATH + String.format(Config.RELATION_QUERY, uid);
      APIResponse response = request.get(url);

      if (!response.ok()) {
        System.err.printf("Error changing relation for UID: %s. Response text: %s%n", uid, response.statusText());
      }
    }

    IO.println("Finished changing relations for %d UIDs.".formatted(end - start + 1));
  }
}
