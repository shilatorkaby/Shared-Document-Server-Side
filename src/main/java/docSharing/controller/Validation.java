package docSharing.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PASSWORD_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9]{5,10}$", Pattern.CASE_INSENSITIVE);


    private static boolean validPassword(String password) {
        Matcher matcher = VALID_PASSWORD_ADDRESS_REGEX.matcher(password);
        return matcher.find();
    }
    public static void isValidPassword(String password) {
        if (validPassword(password)) return;
        throw new IllegalArgumentException("The password you entered is not valid.");
    }

    private boolean validEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public void isValidEmail(String email) {
        if (validEmail(email)) return;
        throw new IllegalArgumentException("The email you entered is not valid.");
    }
}
