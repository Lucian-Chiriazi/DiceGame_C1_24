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

    public String getPlayerName() {
        return playerName;
    }

    public String[] getPlayerScores() {
        return playerScores;
    }

    public int getDiceLeft() {
        return diceLeft;
    }

    public boolean getPlayedTurn() {
        return playedTurn;
    }

    public String getTotalScore () {
        int totalScore = 0;
        for(String score : this.playerScores) {
            if (score == null) {
                totalScore += 0;
            }else {
                totalScore += Integer.parseInt(score);
            }
        }
        return Integer.toString(totalScore);
    }

    public String getPlayerCategoryScore(int category) {
        return playerScores[category];
    }

    public void setDiceLeft(int diceKept) {
        this.diceLeft -= diceKept;
    }

    public void setPlayedTurn(boolean playedTurn) {
        this.playedTurn = playedTurn;
    }

    public void setPlayerScores(int category, int score) {
        this.playerScores[category - 1] = Integer.toString(score);
    }

    public void resetVariables() {
        this.diceLeft = 5;
        this.playedTurn = false;
    }

}
