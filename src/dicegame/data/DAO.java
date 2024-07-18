package dicegame.data;

import dicegame.business.Player;
import java.util.ArrayList;

public interface DAO {
    ArrayList<Player> getPlayers();
}
