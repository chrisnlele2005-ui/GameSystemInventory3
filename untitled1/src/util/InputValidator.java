package util;

public class InputValidator {
    // Tjek om en tekst kan konverteres til double
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Tjek om en tekst kan konverteres til int
    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Tjek at streng ikke er tom
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
