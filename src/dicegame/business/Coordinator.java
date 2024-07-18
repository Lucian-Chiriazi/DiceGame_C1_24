package dicegame.business;

import dicegame.data.DAO;
import dicegame.data.SimpleDAOImplementation;

import java.util.ArrayList;
import java.util.Scanner;

public class Coordinator {

    private DAO dao;
    private ArrayList<Player> players;
    private int round;
    private boolean forfeit;
    private int currentPlayer;

    public Coordinator() {
        dao = new SimpleDAOImplementation();
        this.players = dao.getPlayers();
        this.round = 7;
        this.forfeit = false;
        this.currentPlayer = 0;
    }

    public void startGameLogic() {
        while (round < 8 && !forfeit) {
            startRound();
        }
    }

    private void startRound() {
        System.out.println(printScoreBoard());
        System.out.println(printRound());

        playTurn(players.get(currentPlayer));
    }

    private void playTurn(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(printMessage1(player));
        System.out.println();
        System.out.println(printMessage2(player));

        String input = scanner.nextLine().trim();



    }


    private StringBuilder printMessage1 (Player player) {
        StringBuilder temp = new StringBuilder();
        temp.append("First throw of this turn, ");
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

}
