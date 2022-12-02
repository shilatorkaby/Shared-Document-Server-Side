package docSharing.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    private static Logger logger = LogManager.getLogger(Validate.class.getName());
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^[0-9]{5,10}$", Pattern.CASE_INSENSITIVE);

    public static boolean email(String email) {

        logger.info("email validation has begun: ");
        boolean status = VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();

        if (status) {
            logger.info("email is valid");
        } else {
            logger.warn("email is not valid");
        }
        return status;
    }

    public static boolean password(String password) {
        logger.info("password validation has begun: ");
        boolean status = VALID_PASSWORD_REGEX.matcher(password).find();

        if (status) {
            logger.info("password is valid");
        } else {
            logger.warn("password is not valid");
        }
        return status;
    }
//
//
//    private static boolean validPassword(String password) {
//        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
//        logger.info("validPassword complete");
//        return matcher.find();
//    }
//
//    public static void isValidPassword(String password) {
//        if (validPassword(password)) return;
//        logger.error("validPassword NOT complete");
//        throw new IllegalArgumentException("The password you entered is not valid.");
//    }
//
//    private boolean validEmail(String email) {
//        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
//        logger.info("validEmail complete");
//        return matcher.find();
//    }
//
//    public void isValidEmail(String email) {
//        if (validEmail(email)) return;
//        logger.error("validEmail NOT complete");
//        throw new IllegalArgumentException("The email you entered is not valid.");
//    }
}
