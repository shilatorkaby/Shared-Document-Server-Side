package docSharing.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private void validPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password can't be empty!");
        } else if (password.length() < 8 || password.length() > 12) {
            throw new IllegalArgumentException("Password length invalid. The passwords length should be between 8 and 12 characters.");
        }
    }

    public void isValidPassword(String password) {
        try {
            validPassword(password);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The password you typed is invalid. " + e.getMessage());
        }
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
