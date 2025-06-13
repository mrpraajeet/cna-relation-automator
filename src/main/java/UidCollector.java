import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class UidCollector {
    public UidCollector(Page page) {
        page.navigate(Config.BASE_URL + Config.LEADERBOARD_PATH + Config.GAME_PATH);
        Set<String> uids = new LinkedHashSet<>();

        for(int i = 1; i <= Config.LEADERBOARD_PAGES; i++) {
            System.out.println("Page " + i + "is underway. Total UIDs: " + uids.size());

            try {
                page.waitForSelector(
                    Config.PLAYER_SELECTOR,
                    new Page.WaitForSelectorOptions().setTimeout(Config.PAGINATION_TIMEOUT)
                );
            } catch (TimeoutError e) {
                //page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("no_players_" + i + ".png")));
                System.err.println("No player links on page " + i + ".");
                break;
            }

            for (Locator playerLink : page.locator(Config.PLAYER_SELECTOR).all()) {
                String href = playerLink.getAttribute("href");
                if (href != null) {
                    Matcher matcher = Config.UID_PATTERN.matcher(href);
                    if (matcher.find()) {
                        uids.add(matcher.group(1));
                    } else {
                        System.out.println("No UID found for player link: " + href);
                    }
                } else {
                    System.out.println("No href attribute for player link.");
                }
            }

            Locator nextPage = page.locator(Config.NEXT_PAGE_SELECTOR);
            if (nextPage.count() > 0 && nextPage.isEnabled()) {
                nextPage.click();
                try {
                    page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(10000));
                    page.waitForTimeout(1000);
                } catch (TimeoutError e) {
                    //page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("loading_delay_" + i + ".png")));
                    System.err.println("Page" + i + "has not finished loading.");
                    break;
                }
            } else {
                //page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("no_button_" + i + ".png")));
                System.out.println("No next page button on page " + i + ".");
                break;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Config.UIDS_FILE))) {
            for (String uid : uids) {
                writer.write(uid);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
