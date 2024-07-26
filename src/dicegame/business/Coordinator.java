package dicegame.business;

import com.sun.xml.internal.bind.v2.TODO;
import dicegame.data.DAO;
import dicegame.data.SimpleDAOImplementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class Coordinator {

    private DAO dao;
    private final List<Integer> sequence1;
    private final List<Integer> sequence2;
    private ArrayList<Player> players;
    private int round;
    private boolean forfeit;
    private boolean sequenceActive;
    private boolean sequenceAchieved;
    private int currentPlayer;
    private int turnsLeft;
    private String activeCategory;
    private ArrayList<Integer> currentThrow;
    private ArrayList<Integer> currentDiceKept;
    private Scanner scanner;

    public Coordinator() {
        dao = new SimpleDAOImplementation();
        this.sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        this.sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        this.players = dao.getPlayers();
        this.round = 6;
        this.forfeit = false;
        this.sequenceActive = false;
        this.sequenceAchieved = false;
        this.currentPlayer = 0;
        this.turnsLeft = 3;
        this.activeCategory = "";
        this.currentThrow = new ArrayList<>();
        this.currentDiceKept = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void startGameLogic() {
        System.out.println(printScoreBoard());
        while (round < 8 && !forfeit) {
            startRound();
            round++;
        }
        scanner.close();
    }

    private void startRound() {
        this.currentPlayer = 0;

        System.out.println(printRound());
        for (int i = 0; i < players.size(); i++) {
            playTurn(players.get(currentPlayer));
            System.out.println("\n");
            System.out.println(printScoreBoard());
            resetVariables(players.get(currentPlayer));
            currentPlayer++;
        }

    }

    private void playTurn(Player player) {
        System.out.println(printMessage1(player));
        System.out.println();
        System.out.print(printMessage2(player));

        String input = scanner.nextLine().trim();

        while (!Validation.throwValidation(input)) {
            System.out.print(printMessage3());
            input = scanner.nextLine().trim();
        }

        if (input.equals("t")) {
            turnsLeft--;
            System.out.println();
            System.out.println(printThrowsLeft());

            this.currentThrow = generateThrow(player);
            System.out.println(printThrow());
            System.out.print(printMessage4());

            String input2 = scanner.nextLine().trim();

            while (!Validation.selectOrDeferInputValidation(input2)) {
                System.out.print(printMessage5());
                input2 = scanner.nextLine().trim();
            }

            if (input2.equals("s")) {
                System.out.println("Select category to play\n");
                System.out.print(printCategoryOptions());
                String input3 = scanner.nextLine().trim();

                while (!Validation.categoryValidation(input3, player, this.currentThrow)) {
                    System.out.println(printMessage6());
                    System.out.print(printCategoryOptions());
                    input3 = scanner.nextLine().trim();
                }

                System.out.println(printCategoryName(input3));

                if (input3.equals("7")) {
                    sequenceActive = true;
                    playSequence(player);
                }else {
                    processThrowAndPrintInfo(input3);
                    player.setDiceLeft(countOccurrences(input3));
                    this.activeCategory = input3;

                    while (this.turnsLeft != 0 && !forfeit) {
                        playNext(player);
                    }
                    player.setPlayerScores(Integer.parseInt(activeCategory), calculateTurnScore());
                    System.out.println("\n");
                    System.out.println(printMessage7(player));
                }
            }else {
                System.out.println("Selection deferred.\n");
                if (turnsLeft != 0) {
                    playTurn(player);
                }
            }
        }else {
            initialiseForfeitProcedure();
        }
    }

    private void playNext(Player player) {
        System.out.println("\n");
        System.out.println(printMessage1(player));
        System.out.print(printMessage2(player));

        String input = scanner.nextLine().trim();

        while (!Validation.throwValidation(input)) {
            System.out.print(printMessage3());
            input = scanner.nextLine().trim();
        }

        if (input.equals("t")) {
            turnsLeft--;
            this.currentThrow = generateThrow(player);
            System.out.println();
            System.out.println(printThrow());
            if (sequenceActive){
                if (turnsLeft == 0) {
                    for (Integer value : currentThrow) {
                        currentDiceKept.add(value);
                        if (currentDiceKept.equals(sequence1) || currentDiceKept.equals(sequence2)) {
                            sequenceActive = true;
                        }
                    }
                }
                playSequence(player);
                
            }else {
                processThrowAndPrintInfo(activeCategory);
                player.setDiceLeft(countOccurrences(activeCategory));
            }
        }else {
            initialiseForfeitProcedure();
        }
    }

    private void playSequence(Player player) {
        System.out.println();
        System.out.println(printSequence());
        System.out.print(printMessage8());
        String input = scanner.nextLine().trim();

        while (!Validation.sequenceValidation(input, currentThrow)) {
            System.out.println("Not a valid input!");
            System.out.print(printMessage8());
            input = scanner.nextLine().trim();
        }
        if (turnsLeft >= 0 && !sequenceAchieved) {
            if (input.equals("0")) {
                System.out.println(printMessage9());
                playNext(player);
            } else {
                switch (turnsLeft) {
                    case 2:
                        System.out.println("You have selected the following dice to keep.");
                        processSequence(input, player);
                        playNext(player);
                    case 1:
                        System.out.println("You have selected the following dice to keep.");
                        processSequence(input, player);
                        System.out.println(printMessage9());
                        playNext(player);
                }
            }
        }else {

        }
    }

    private StringBuilder printMessage1(Player player) {
        StringBuilder temp = new StringBuilder();

        if (this.turnsLeft == 3) {
            temp.append("First throw of this turn, ");
        }else if(this.turnsLeft == 2) {
            temp.append("Second throw of this turn, ");
        }else {
            temp.append("Final throw of this turn, ");
        }
        temp.append(player.getPlayerName());
        temp.append(" to throw ");
        temp.append(player.getDiceLeft());
        temp.append(" dice.");

        return temp;
    }

    private StringBuilder printMessage2(Player player) {
        StringBuilder temp = new StringBuilder();
        temp.append("Throw ");
        temp.append(player.getDiceLeft());
        temp.append(" dice, enter 't' to throw or 'f' to forfeit >  ");

        return temp;
    }

    private StringBuilder printMessage3() {
        StringBuilder temp = new StringBuilder();
        temp.append("Invalid input\n");
        temp.append("Enter 't' to throw or 'f' to forfeit > ");

        return temp;
    }

    private StringBuilder printMessage4() {
        StringBuilder temp = new StringBuilder();
        temp.append("Enter 's' to select category (number on die/dice) or 'd' to defer >");

        return temp;
    }

    private StringBuilder printMessage5() {
        StringBuilder temp = new StringBuilder();
        temp.append("Invalid input\n");
        temp.append("Enter 's' to select category (number on die/dice) or 'd' to defer >");

        return temp;
    }

    private StringBuilder printMessage6() {
        StringBuilder temp = new StringBuilder();
        temp.append("Invalid input\n");
        temp.append("Select a category not chosen before to play.\n");

        return temp;
    }

    private StringBuilder printMessage7 (Player player) {
        StringBuilder temp = new StringBuilder();
        temp.append(player.getPlayerName());
        temp.append(" made ");
        temp.append(currentDiceKept.size());
        temp.append(" with value ");
        temp.append(activeCategory);
        temp.append(" and scores ");
        temp.append(calculateTurnScore());
        temp.append(" for that round");
        return temp;
    }

    private StringBuilder printMessage8() {
        StringBuilder temp = new StringBuilder();
        temp.append("\n");
        temp.append("Enter which dice you wish to set aside using the number labels separated by a space (e.g. 1, 3, 5) or enter 0 for none > ");

        return temp;
    }

    private StringBuilder printMessage9() {
        StringBuilder temp = new StringBuilder();
        temp.append("You have not selected any dice to keep from that throw.\n");
        if (currentDiceKept.size() > 0) {
            temp.append("You have the following dice set aside before the next throw\n");
            for (Integer value : currentDiceKept) {
                temp.append(" [").append(value).append("]");
            }
        }
        return temp;
    }

    private StringBuilder printThrow() {
        StringBuilder temp = new StringBuilder();

        temp.append("Throw:");
        for (Integer value : currentThrow) {
            temp.append(" [").append(value).append("]");
        }

        return temp;
    }

    private StringBuilder printThrowsLeft() {
        StringBuilder temp = new StringBuilder();
        temp.append(turnsLeft);
        temp.append(" throws remaining for this turn.\n");

        return temp;
    }

    private StringBuilder printRound() {
        StringBuilder round = new StringBuilder();

        round.append("-------\n");
        round.append("Round ");
        round.append(this.round);
        round.append("\n");
        round.append("-------\n");

        return round;
    }

    private StringBuilder printScoreBoard() {
        StringBuilder scoreboard = new StringBuilder();

        scoreboard.append("------------------------------------------\n");
        scoreboard.append("| Category     |  Player 1  |  Player 2  |\n");
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Ones",
                (players.get(0).getPlayerCategoryScore(0) == null) ? " " : players.get(0).getPlayerCategoryScore(0),
                (players.get(1).getPlayerCategoryScore(0) == null) ? " " : players.get(1).getPlayerCategoryScore(0)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Twos",
                (players.get(0).getPlayerCategoryScore(1) == null) ? " " : players.get(0).getPlayerCategoryScore(1),
                (players.get(1).getPlayerCategoryScore(1) == null) ? " " : players.get(1).getPlayerCategoryScore(1)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Threes",
                (players.get(0).getPlayerCategoryScore(2) == null) ? " " : players.get(0).getPlayerCategoryScore(2),
                (players.get(1).getPlayerCategoryScore(2) == null) ? " " : players.get(1).getPlayerCategoryScore(2)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Fours",
                (players.get(0).getPlayerCategoryScore(3) == null) ? " " : players.get(0).getPlayerCategoryScore(3),
                (players.get(1).getPlayerCategoryScore(3) == null) ? " " : players.get(1).getPlayerCategoryScore(3)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Fives",
                (players.get(0).getPlayerCategoryScore(4) == null) ? " " : players.get(0).getPlayerCategoryScore(4),
                (players.get(1).getPlayerCategoryScore(4) == null) ? " " : players.get(1).getPlayerCategoryScore(4)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Sixes",
                (players.get(0).getPlayerCategoryScore(5) == null) ? " " : players.get(0).getPlayerCategoryScore(5),
                (players.get(1).getPlayerCategoryScore(5) == null) ? " " : players.get(1).getPlayerCategoryScore(5)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-11s  |     %2s     |     %2s     |\n",
                "Sequence 20",
                (players.get(0).getPlayerCategoryScore(6) == null) ? " " : players.get(0).getPlayerCategoryScore(6),
                (players.get(1).getPlayerCategoryScore(6) == null) ? " " : players.get(1).getPlayerCategoryScore(6)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Total",
                players.get(0).getTotalScore(),
                players.get(1).getTotalScore()));
        scoreboard.append("------------------------------------------\n");

        return scoreboard;
    }

    private StringBuilder printCategoryOptions() {
        StringBuilder temp = new StringBuilder();
        temp.append("Ones (1) Twos (2) Threes (3) Fours (4) Fives (5) Sixes (6) Sequence (7) >");
        return temp;
    }

    private StringBuilder printCategoryName(String input) {
        StringBuilder temp = new StringBuilder();
        switch (input) {
            case "1" :
                temp.append("Ones selected.");
                break;
            case "2" :
                temp.append("Twos selected.");
                break;
            case "3" :
                temp.append("Threes selected.");
                break;
            case "4" :
                temp.append("Fours selected.");
                break;
            case "5" :
                temp.append("Fives selected.");
                break;
            case "6" :
                temp.append("Sixes selected.");
                break;
            case "7" :
                temp.append("Sequence 20 selected.");
                break;
        }
        return temp;
    }

    private StringBuilder printSequence() {
        StringBuilder temp = new StringBuilder();
        int index = 1;
        temp.append("0. None");
        temp.append("\n");
        for (Integer value : currentThrow) {
            temp.append(index);
            temp.append(". ");
            temp.append(" [").append(value).append("]");
            temp.append("\n");
            index++;
        }

        return temp;
    }

    private ArrayList<Integer> generateThrow(Player player) {
        ArrayList<Integer> temp = new ArrayList<>();
        Random rand = new Random();
        int diceLeft = player.getDiceLeft();
        for (int i = 0; i < diceLeft; i++) {
            temp.add(rand.nextInt(6) + 1);
        }
        return temp;
    }

    private void processThrowAndPrintInfo(String input) {

        int occurrences = countOccurrences(input);
        updateDiceKept(input, occurrences);

        System.out.print("That throw had " + occurrences + " dice with value " + input + ".");
        System.out.print(" Setting aside " + currentDiceKept.size() + " dice:");
        for (Integer value : currentDiceKept) {
            System.out.print(" [" + value + "] ");
        }
    }

    private void processSequence(String input, Player player) {
        String[] values = input.split("\\s+");
        for (String value : values) {
            this.currentDiceKept.add(Integer.parseInt(value));
            System.out.print("[" + value + "]");
        }

        if (currentDiceKept.equals(sequence1) || currentDiceKept.equals(sequence2)) {
            sequenceAchieved = true;

        }else {
            player.setDiceLeft(currentDiceKept.size());
        }

    }

    private int countOccurrences(String input) {
        int count = 0;
        int choice = Integer.parseInt(input);
        for(Integer value : this.currentThrow) {
            if(value == choice) {
                count++;
            }
        }
        return count;
    }

    private void updateDiceKept(String input, int occurrences) {
        int choice = Integer.parseInt(input);
        for (int i = 0; i < occurrences; i++) {
            currentDiceKept.add(choice);
        }
    }

    private int calculateTurnScore() {
        int totalScore = 0;
        for (Integer value : currentDiceKept) {
            totalScore += value;
        }
        return totalScore;
    }

    private void initialiseForfeitProcedure() {
        this.forfeit = true;
        System.out.println(printScoreBoard());
        System.out.println("Game Over!");
        System.exit(0);
    }

    private void resetVariables(Player player) {
        this.activeCategory = " ";
        this.currentThrow = new ArrayList<>();
        this.currentDiceKept = new ArrayList<>();
        this.turnsLeft = 3;
        this.sequenceActive = false;
        player.resetVariables();
    }

}
