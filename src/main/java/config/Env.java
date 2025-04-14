package config;

import io.github.cdimascio.dotenv.Dotenv;

public final class Env {
    /**
     * The Dotenv instance used to load environment variables from a .env file.
     */
    private static final Dotenv DOTENV;

    static {
        String envFile = System.getenv("ENV_FILE");

        if (envFile != null && envFile.equals("docker")) {
            System.out.println("For docker I come here");
            // Load .env.docker for Docker environments
            DOTENV = Dotenv.configure()
                    .filename(".env.docker")
                    .directory("/app")
                    .load();
        } else {
            System.out.println("For otherwise I come here");
            // Load .env for local environments
            DOTENV = Dotenv.configure().filename(".env").load();
        }
    }

    /**
     * Private constructor to prevent instantiation of the Env class.
     */
    // Private constructor to prevent instantiation
    private Env() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param key The key of the environment variable.
     * @return The value of the environment variable.
     */
    public static String get(final String key) {
        return DOTENV.get(key);
    }
}
