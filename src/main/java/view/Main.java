package view;

public final class Main {
    // Private constructor to prevent instantiation
    private Main() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(final String[] args) {
        View.launch(View.class);
    }
}