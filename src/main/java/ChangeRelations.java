import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ChangeRelations {
    private final APIRequestContext requestContext;

    public ChangeRelations(APIRequestContext requestContext) {
        this.requestContext = requestContext;
        run();
    }

    private void run() {
        List<String> uids = new ArrayList<>();
        try (InputStream stream = ChangeRelations.class.getResourceAsStream(Config.UIDS_FILE)) {
            if (stream == null) {
                System.err.println("Could not find " + Config.UIDS_FILE);
                return;
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
            System.err.println("Error reading " + Config.UIDS_FILE + ": " + e.getMessage());
            return;
        }

        try (ExecutorService exe = Executors.newFixedThreadPool(Config.CONCURRENT_REQUESTS)) {
            AtomicInteger processed = new AtomicInteger(0);

            for (String id : uids) {
                exe.submit(() -> {
                    APIResponse response = null;
                    try {
                        int currentIndex = processed.incrementAndGet();
                        System.out.println("Changing " + Config.USER_NAME + "'s relation towards "
                            + id + " (" + currentIndex + "/" + uids.size() + ")");

                        String relativeUrl = Config.PROFILE_PATH
                            + "/" + id
                            + Config.GAME_PATH
                            + Config.PROFILE_PARAM
                            + id
                            + Config.RELATION_ARG;

                        response = requestContext.get(relativeUrl,
                            RequestOptions.create()
                                .setHeader("X-Requested-With", "XMLHttpRequest")
                                .setTimeout(Config.API_TIMEOUT)
                        );

                        System.out.println("Status for ID " + id + ": " + response.statusText());
                        Thread.sleep(Config.API_DELAY);
                    } catch (PlaywrightException e) {
                        System.err.println("Network error for ID " + id + ": " + e.getMessage());
                    } catch (InterruptedException e) {
                        System.err.println("Thread interrupted for ID " + id + ": " + e.getMessage());
                        Thread.currentThread().interrupt();
                    } finally {
                        if (response != null) { response.dispose(); }
                    }
                });
            }
        }

        this.requestContext.dispose();
    }
}
