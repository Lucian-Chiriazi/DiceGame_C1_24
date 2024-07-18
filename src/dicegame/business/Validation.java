package dicegame.business;

import java.util.regex.Pattern;

public class Validation {
    public static boolean startGameValidation (String input) {
        String regex = "^[01]$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input).matches();
    }

    public static boolean throwValidation (String input) {
        String regex = "^[ft]$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input).matches();
    }

    public static boolean categoryValidation (String input) {
        String regex = "^[1234567]$"; // A set of rules for the number. (1 to 7) and 1 character long.
        Pattern pattern = Pattern.compile(regex); // Compile the regex into a pattern.
        return pattern.matcher(input).matches();
    }
}
