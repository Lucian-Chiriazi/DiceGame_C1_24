package dicegame.business;

import java.util.regex.Pattern;

public class Validation {
    public static boolean startGameValidation (String input) {
        String regex = "^[01]$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input).matches();
    }
}
