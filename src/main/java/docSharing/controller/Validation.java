package docSharing.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static Logger logger = LogManager.getLogger(Validation.class.getName());
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PASSWORD_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9]{5,10}$", Pattern.CASE_INSENSITIVE);


    private static boolean validPassword(String password) {
        Matcher matcher = VALID_PASSWORD_ADDRESS_REGEX.matcher(password);
        logger.info("validPassword complete");
        return matcher.find();
    }
    public static void isValidPassword(String password) {
        if (validPassword(password)) return;
        logger.error("validPassword NOT complete");
        throw new IllegalArgumentException("The password you entered is not valid.");
    }

    private boolean validEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        logger.info("validEmail complete");
        return matcher.find();
    }

    public void isValidEmail(String email) {
        if (validEmail(email)) return;
        logger.error("validEmail NOT complete");
        throw new IllegalArgumentException("The email you entered is not valid.");
    }
}
