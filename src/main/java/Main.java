import com.microsoft.playwright.*;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserType.LaunchOptions;

void main(String[] args) {
  List<String> argList = Arrays.asList(args);
  boolean isScrapingMode = argList.contains("--scraping");
  boolean isBrowserHeadless = argList.contains("--headless");

  BiFunction<String, Integer, Integer> getIntArg = (argName, defaultValue) -> {
    int argIndex = argList.indexOf(argName);
    return (argIndex >= 0 && argList.size() > argIndex + 1)
      ? Integer.parseInt(argList.get(argIndex + 1))
      : defaultValue;
  };

  int start = getIntArg.apply("--start", 1);
  int end = getIntArg.apply("--end", Integer.MAX_VALUE);

  try (Playwright playwright = Playwright.create()) {
    try (Browser browser = playwright.chromium().launch(new LaunchOptions().setHeadless(isBrowserHeadless))) {
      BrowserContext context = browser.newContext(new NewContextOptions().setBaseURL(Config.BASE_URL));
      Page page = Auth.login(context);
      APIRequestContext request = context.request();

      if (isScrapingMode) {
        new Scraper(page, start, end);
      } else {
        new Requestor(request, start, end);
      }

      context.close();
      page.close();
    }
  } catch (PlaywrightException e) {
    System.err.println("Error occurred: " + e.getMessage());
  }
}