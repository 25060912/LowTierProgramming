package logic.loginDatabase;

public class DatabaseConfig {
    public static final String DB_URL = System.getenv("DB_URL");
    public static final String USER = System.getenv("DB_USER");
    public static final String PASS = System.getenv("DB_PASS");

    static {
        if (DB_URL == null || USER == null || PASS == null) {
            throw new RuntimeException("Database environment variables not set!");
        }
    }
}