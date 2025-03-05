package util;

public class AuthManager {
    private static AuthManager instance;
    private String jwtToken;

    private AuthManager() {}

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public void setToken(String token) {
        this.jwtToken = token;
    }

    public String getToken() {
        return jwtToken;
    }

    public boolean isAuthenticated() {
        return jwtToken != null;
    }

}
