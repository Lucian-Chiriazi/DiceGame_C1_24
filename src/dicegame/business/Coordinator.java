package dicegame.business;

import dicegame.data.DAO;
import dicegame.data.SimpleDAOImplementation;

import java.util.ArrayList;

public class Coordinator {

    private DAO dao;
    private ArrayList<Player> players;

    public Coordinator() {
        dao = new SimpleDAOImplementation();
        this.players = dao.getPlayers();
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
