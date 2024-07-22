package dicegame.business;

import java.util.ArrayList;
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

    public static boolean selectOrDeferInputValidation(String input) {
        String regex = "^[sd]$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input).matches();
    }

    public static boolean categoryValidation (String input, Player player, ArrayList<Integer> currentThrow) {
        boolean temp = false;
        String regex = "^[1234567]$"; // A set of rules for the number. (1 to 7) and 1 character long.
        Pattern pattern = Pattern.compile(regex); // Compile the regex into a pattern.
        if (pattern.matcher(input).matches()) {
            if (categoryAvailable(input, player, currentThrow)) {
                temp = true;
            }
        }
        return temp;
    }

    public static boolean categoryAvailable (String inputCharacter,Player player, ArrayList<Integer> currentThrow) {
        boolean temp = false;
        int userChoice = Integer.parseInt(inputCharacter);
        String[] temp2 = player.getPlayerScores();

        if (temp2[userChoice - 1] == null) { // Check to see if the category has already been chosen
            for (Integer value : currentThrow) { // Check to see if the choice exists in current throw
                if (value == userChoice) {
                    temp = true;
                }
            }
        }else {
            System.out.println("You already chose that category!");
        }
        return temp;
    }
}
