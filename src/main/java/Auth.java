import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class Auth {
  public static Page login(BrowserContext context) {
    Page page = context.newPage();
    IO.println("Navigating to login page");
    page.navigate(Config.LOGIN_PATH);

    IO.println("Filling in login form");
    page.locator(Config.EMAIL_SELECTOR).fill(Config.EMAIL);
    page.locator(Config.PASSWORD_SELECTOR).fill(Config.PASSWORD);
    page.click(Config.SUBMIT_SELECTOR);

    page.waitForLoadState(LoadState.NETWORKIDLE);
    IO.println("Successfully logged in");
    return page;
  }
}
