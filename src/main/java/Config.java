import java.util.function.Function;
import java.util.regex.Pattern;

public final class Config {
  private Config() {}

  public static final String USERNAME = System.getenv("USERNAME");
  public static final String EMAIL = System.getenv("EMAIL");
  public static final String PASSWORD = System.getenv("PASSWORD");

  public static final String EMAIL_SELECTOR = "#frm-signInForm-form-email";
  public static final String PASSWORD_SELECTOR = "#frm-signInForm-form-password";
  public static final String SUBMIT_SELECTOR = "#frm-signInForm-form input.btn.btn-red";

  public static final String BASE_URL = new StringBuilder("moc.semaghcezc.tnuocca//:sptth").reverse().toString();
  public static final String LOGIN_PATH = "/sign/in";
  public static final String LEADERBOARD_PATH = "/leaderboards";
  public static final String GAME_PATH = new StringBuilder("ne-semanedoc-203/").reverse().toString();
  public static final String PROFILE_PATH = "/profile/detail/%s";
  public static final String RELATION_QUERY = "?profileBox-id=%s&do=profileBox-ignoreUser"; // addFriend unsetRelation

  public static final String UIDS_FILE = "uids.txt";
  public static final Pattern UID_PATTERN = Pattern.compile("/profile/detail/(\\d+)/");

  private static final String LEADERBOARD_SELECTOR = "#snippet-gameLeaderboard-dataTable";
  public static final String PLAYER_SELECTOR = LEADERBOARD_SELECTOR + " td.login a";
  private static final Function<String, String> BTN_SELECTOR = (icon) -> LEADERBOARD_SELECTOR + " div.text-center a.ajax.btn.btn-red:has(i." + icon + ")";
  public static final Function<Integer, String> PAGE_QUERY =  (num) -> "?gameLeaderboard-page=" + num + "&gameLeaderboard-table=all&do=gameLeaderboard-changePage";
  public static final String NEXT_PAGE_SELECTOR = BTN_SELECTOR.apply("fa-angle-right"); // left double-left double-right
}
