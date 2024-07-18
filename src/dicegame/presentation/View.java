package dicegame.presentation;

import dicegame.business.Coordinator;

import java.util.Scanner;

public class View {
    private Scanner scanner = new Scanner(System.in);
    private Coordinator coordinator;

    public View (Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
