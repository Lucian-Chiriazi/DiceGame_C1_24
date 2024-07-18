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
}
