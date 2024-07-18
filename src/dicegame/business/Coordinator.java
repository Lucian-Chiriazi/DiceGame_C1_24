package dicegame.business;

import com.sun.xml.internal.bind.v2.TODO;
import dicegame.data.DAO;
import dicegame.data.SimpleDAOImplementation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Coordinator {

    private DAO dao;
    private ArrayList<Player> players;
    private int round;
    private boolean forfeit;
    private int currentPlayer;
    private int turnsLeft;
    private String activeCategory;
    private ArrayList<Integer> currentThrow;
    private ArrayList<Integer> currentDiceKept;

    public Coordinator() {
        dao = new SimpleDAOImplementation();
        this.players = dao.getPlayers();
        this.round = 7;
        this.forfeit = false;
        this.currentPlayer = 0;
        this.turnsLeft = 3;
        this.activeCategory = "";
        this.currentThrow = new ArrayList<>();
        this.currentDiceKept = new ArrayList<>();
    }

    public void startGameLogic() {
        System.out.println(printScoreBoard());
        while (round < 8 && !forfeit) {
            startRound();
            round++;
        }
    }

    private void startRound() {
        if (round != 7) {
            resetVariables();
        }

        System.out.println(printRound());
        playTurn(players.get(currentPlayer));
    }

    private void playTurn(Player player) {
        Scanner scanner = new Scanner(System.in);
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
                    playSequence();
                }else {
                    processThrowAndPrintInfo(input3, player);
                    player.setDiceLeft(countOccurrences(input3));
                    this.activeCategory = input3;
                }
            }
        }else {
            initialiseForfeitProcedure();
        }

        scanner.close();
    }


    private StringBuilder printMessage1 (Player player) {
        StringBuilder temp = new StringBuilder();

        if (this.turnsLeft == 3) {
            temp.append("First throw of this turn, ");
        }else {
            temp.append("Next throw of this turn, ");
        }

        temp.append(player.getPlayerName());
        temp.append(" to throw ");
        temp.append(player.getDiceLeft());
        temp.append(" dice.");

        return temp;
    }

    private StringBuilder printMessage2 (Player player) {
        StringBuilder temp = new StringBuilder();
        temp.append("Throw ");
        temp.append(player.getDiceLeft());
        temp.append(" dice, enter 't' to throw or 'f' to forfeit >  ");

        return temp;
    }

    private StringBuilder printMessage3 () {
        StringBuilder temp = new StringBuilder();
        temp.append("Invalid input\n");
        temp.append("Enter 't' to throw or 'f' to forfeit > ");

        return temp;
    }

    private StringBuilder printMessage4 () {
        StringBuilder temp = new StringBuilder();
        temp.append("Enter 's' to select category (number on die/dice) or 'd' to defer >");

        return temp;
    }

    private StringBuilder printMessage5 () {
        StringBuilder temp = new StringBuilder();
        temp.append("Invalid input\n");
        temp.append("Enter 's' to select category (number on die/dice) or 'd' to defer >");

        return temp;
    }

    private StringBuilder printMessage6 () {
        StringBuilder temp = new StringBuilder();
        temp.append("Invalid input\n");
        temp.append("Select a category not chosen before to play.\n");

        return temp;
    }

    private void playSequence() {
        //TODO: finish this
    }

    private StringBuilder printThrow() {
        StringBuilder temp = new StringBuilder();

        temp.append("Throw:");
        for (Integer value : currentThrow) {
            temp.append(" [").append(value).append("]");
        }

        return temp;
    }

    private StringBuilder printThrowsLeft () {
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
                (players.get(1).getPlayerCategoryScore(3) == null) ? " " : players.get(0).getPlayerCategoryScore(3)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Fives",
                (players.get(0).getPlayerCategoryScore(4) == null) ? " " : players.get(0).getPlayerCategoryScore(4),
                (players.get(1).getPlayerCategoryScore(4) == null) ? " " : players.get(0).getPlayerCategoryScore(4)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-8s     |     %2s     |     %2s     |\n",
                "Sixes",
                (players.get(0).getPlayerCategoryScore(5) == null) ? " " : players.get(0).getPlayerCategoryScore(5),
                (players.get(1).getPlayerCategoryScore(5) == null) ? " " : players.get(0).getPlayerCategoryScore(5)));
        scoreboard.append("------------------------------------------\n");
        scoreboard.append(String.format("| %-11s  |     %2s     |     %2s     |\n",
                "Sequence 20",
                (players.get(0).getPlayerCategoryScore(6) == null) ? " " : players.get(0).getPlayerCategoryScore(6),
                (players.get(1).getPlayerCategoryScore(6) == null) ? " " : players.get(0).getPlayerCategoryScore(6)));
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

    private StringBuilder printCategoryName (String input) {
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
                temp.append("Sequence selected.");
                break;
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

    private void processThrowAndPrintInfo (String input, Player player) {

        int occurrences = countOccurrences(input);
        updateDiceKept(input, occurrences);

        System.out.print("That throw had " + occurrences + " dice with value " + input + ".");
        System.out.print(" Setting aside " + occurrences + " dice:");
        for (Integer value : currentDiceKept) {
            System.out.print(" [" + value + "] ");
        }
    }

    private int countOccurrences (String input) {
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

    private void initialiseForfeitProcedure() {
        //TODO: finish this
    }

    private void resetVariables() {
        this.currentPlayer = 0;
        this.turnsLeft = 3;
    }

}
