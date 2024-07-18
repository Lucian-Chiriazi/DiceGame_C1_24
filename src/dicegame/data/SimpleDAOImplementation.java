package dicegame.data;

import dicegame.business.Player;
import java.util.ArrayList;

public class SimpleDAOImplementation implements DAO{

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        players.add(new Player("PlayerOne"));
        players.add(new Player("PlayerTwo"));

        return players;
    }
}
