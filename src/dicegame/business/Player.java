package dicegame.business;

public class Player {

    private String [] playerScores;
    private String playerName;
    private int diceLeft;
    private boolean playedTurn;

    public Player(String playerName) {
        this.playerScores = new String [7];
        this.playerName = playerName;
        this.diceLeft = 5;
        this.playedTurn = false;
    }


}
