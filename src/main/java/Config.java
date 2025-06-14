import java.util.regex.Pattern;

public final class Config {
    private Config() {}

    public static final String USER_NAME = "Guest";
    public static final String EMAIL = System.getenv("EMAIL");
    public static final String PASSWORD = System.getenv("PASSWORD");

    public static final boolean COLLECT_UIDS = false;
    public static final boolean CHANGE_RELATIONS = true;
    public static final boolean IS_HEADLESS = false;

    public static final String BASE_URL = new StringBuilder("moc.semaghcezc.tnuocca//:sptth").reverse().toString();
    public static final String LEADERBOARD_PATH = "/leaderboards";
    public static final String GAME_PATH = new StringBuilder("ne-semanedoc-203/").reverse().toString()";
    public static final String PROFILE_PATH = "/profile/detail";
    public static final String LOGIN_PATH = "/sign/in";
    public static final String PROFILE_PARAM = "?profileBox-id=";
    public static final String RELATION_ARG = "&do=profileBox-ignoreUser"; // -addFriend -unsetRelation

    public static final int LEADERBOARD_PAGES = 3000;
    public static final long PAGINATION_TIMEOUT = 10000L;
    public static final long TYPE_DELAY = 150L;
    public static final long NAV_TIMEOUT = 60000L;
    public static final long API_TIMEOUT = 60000L;
    public static final long API_DELAY = 110L;
    public static final int CONCURRENT_REQUESTS = 2;

    public static final String UIDS_FILE = "/uids.txt";
    public static final Pattern UID_PATTERN = Pattern.compile(PROFILE_PATH + "/(\\d+)/");

    public static final String PLAYER_SELECTOR = "#snippet-gameLeaderboard-dataTable td.login a";
    public static final String NEXT_PAGE_SELECTOR = "#snippet-gameLeaderboard-dataTable div.text-center a.ajax.btn.btn-red:has(i.fa-angle-right)";
    public static final String EMAIL_SELECTOR = "#frm-signInForm-form-email";
    public static final String PASSWORD_SELECTOR = "#frm-signInForm-form-password";
    public static final String SUBMIT_SELECTOR = "#frm-signInForm-form input.btn.btn-red";
}
