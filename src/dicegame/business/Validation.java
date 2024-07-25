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
            if (input.equals("7")) {
                temp = true;
            }
        }
        return temp;
    }

    public static boolean sequenceValidation (String input, ArrayList<Integer> currentThrow) {

        String[] values = input.split("\\s+");
        String regex = "^[012345 ]{1,9}$"; // A set of rules for the sequence input. (0 to 5 including spaces) and between 1 and 9 characters long.
        Pattern pattern = Pattern.compile(regex); // Compile the regex into a pattern.

        boolean patterMatcherCheck = pattern.matcher(input).matches();
        boolean inputSizeCheck = true;
        boolean containZeroCheck = true;

        if (pattern.matcher(input).matches()) {
            for (String value : values) {
                int option = Integer.parseInt(value.trim());
                if (option > currentThrow.size()) {
                    inputSizeCheck = false;
                }
                if (option == 0 && input.length() > 1) {
                    containZeroCheck = false;
                }
            }
        }

        return patterMatcherCheck && inputSizeCheck && containZeroCheck;
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
