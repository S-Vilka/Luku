package util;

/**
 * Singleton class to manage authentication tokens.
 */
public final class AuthManager {
    /**
     * The single instance of the AuthManager.
     */
    private static AuthManager instance;

    /**
     * The JWT token used for authentication.
     */
    private String jwtToken;

    /**
     * Private constructor to prevent instantiation.
     */
    private AuthManager() {
    }

    /**
     * Returns the singleton instance of the AuthManager.
     *
     * @return the singleton instance
     */
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    /**
     * Sets the JWT token.
     *
     * @param token the JWT token to set
     */
    public void setToken(final String token) {
        this.jwtToken = token;
    }

    /**
     * Retrieves the JWT token.
     *
     * @return the JWT token
     */
    public String getToken() {
        return jwtToken;
    }

    /**
     * Checks if the user is authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return jwtToken != null;
    }

    /**
     * Validates the current JWT token.
     *
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken() {
        String token = AuthManager.getInstance().getToken();
        if (token == null) {
            return false;
        }
        return JwtUtil.validateToken(token);
    }
}
