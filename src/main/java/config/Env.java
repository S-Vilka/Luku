//package config;
//
//import io.github.cdimascio.dotenv.Dotenv;
//
//public class Env {
//    private static final Dotenv dotenv = Dotenv.load();
//
//    public static String get(String key) {
//        return dotenv.get(key);
//    }
//}

package config;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    private static final Dotenv dotenv;

    static {
        String envFile = System.getenv("ENV_FILE");

        if (envFile != null && envFile.equals("docker")) {
            System.out.println("For docker I come here");
            // Load .env.docker for Docker environments
            dotenv = Dotenv.configure().filename(".env.docker").directory("/app").load();
        } else {
            System.out.println("For otherwise I come here");
            // Load .env for local environments
            dotenv = Dotenv.configure().filename(".env").load();
        }
    }

    public static String get(String key) {
        return dotenv.get(key);
    }
}
