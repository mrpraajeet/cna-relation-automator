import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class Scraper {
  public Scraper(Page page, int start, int end) {
    page.navigate(Config.LEADERBOARD_PATH + Config.GAME_PATH + Config.PAGE_QUERY.apply(start));
    Set<String> uids = new LinkedHashSet<>();

    for (int i = start; i <= end; i++) {
      System.out.printf("Scraping page %d. Total UIDs: %d%n", i, uids.size());

      try {
        scrapeUidsFromPage(page, uids);
      } catch (TimeoutError e) {
        System.err.printf("No player links found on page %d. Attempting to continue.%n", i);
      }

      if (i < end) {
        if (!navigateToNextPage(page, i)) {
          break;
        }
      }
    }

    FileHandler.writeUidsToFile(uids);
  }

  private void scrapeUidsFromPage(Page page, Set<String> uids) {
    for (Locator playerLink : page.locator(Config.PLAYER_SELECTOR).all()) {
      String href = playerLink.getAttribute("href");
      if (href == null) {
        System.err.printf("Unable to find href for %s.", playerLink.innerText());
        continue;
      }

      Matcher matcher = Config.UID_PATTERN.matcher(href);
      if (matcher.find()) {
        uids.add(matcher.group(1));
      } else {
        System.err.printf("Unable to find UID in href for %s.", playerLink.innerText());
      }
    }
  }

  private boolean navigateToNextPage(Page page, int currentPageNum) {
    Locator nextPage = page.locator(Config.NEXT_PAGE_SELECTOR);
    if (nextPage.count() == 0 || !nextPage.isEnabled()) {
      System.err.printf("No next page button found on page %d. Breaking.%n", currentPageNum);
      return false;
    }

    try {
      page.waitForResponse(
        res -> res.url().contains("do=gameLeaderboard-changePage")
          && res.url().contains("gameLeaderboard-page=" + (currentPageNum + 1))
          && res.ok(),
        new Page.WaitForResponseOptions(),
        nextPage::click
      );
      return true;
    } catch (TimeoutError e) {
      System.err.printf("Response for page %d timed out. Continuing anyway.%n", currentPageNum + 1);
      return true;
    }
  }
}
