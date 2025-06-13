import com.microsoft.playwright.*;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Locator.PressSequentiallyOptions;

public class Main {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(Config.IS_HEADLESS)
            );
            BrowserContext context = browser.newContext(
                new NewContextOptions().setBaseURL(Config.BASE_URL)
            );
            Page page = context.newPage();

            if (Config.IS_HEADLESS) { page.setViewportSize(1280, 1024); }

            System.out.println("Logging in...");
            page.navigate(
                Config.LOGIN_PATH,
                new NavigateOptions().setTimeout(Config.NAV_TIMEOUT)
            );

            PressSequentiallyOptions pressOptions = new PressSequentiallyOptions().setDelay(Config.TYPE_DELAY);
            page.locator(Config.EMAIL_SELECTOR)
                .pressSequentially(Config.EMAIL, pressOptions);
            page.locator(Config.PASSWORD_SELECTOR)
                .pressSequentially(Config.PASSWORD, pressOptions);

            page.click(Config.SUBMIT_SELECTOR);
            System.out.println("Logged in.");

            if (Config.COLLECT_UIDS) { new UidCollector(page); }
            if (Config.CHANGE_RELATIONS) { new ChangeRelations(context.request()); }

            page.close();
            context.close();
            browser.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
