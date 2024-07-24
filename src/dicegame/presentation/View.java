package dicegame.presentation;

import dicegame.business.Coordinator;
import dicegame.business.Validation;

import java.util.Scanner;

public class View {
    private Scanner scanner;
    private Coordinator coordinator;

    public View (Coordinator coordinator) {
        this.coordinator = coordinator;
        this.scanner = new Scanner(System.in);
    }

    public void startUI() {
        System.out.println("Strategic dice game");
        System.out.print("Play game (1) or Exit game (0) > ");
        String input = scanner.nextLine().trim();

        while(!Validation.startGameValidation(input)){
            System.out.println("Invalid input");
            System.out.print("Play game (1) or Exit game (0) > ");
            input = scanner.nextLine().trim();
        }
        if ("0".equals(input)) {
            System.out.println("Exit game!");
        }else {
            coordinator.startGameLogic();
        }
    }
}
