package PizzaApp.io;

import java.util.Scanner;

public class DataReader {
    Scanner scanner = new Scanner(System.in);

    public int getInt() {
        try {
            return scanner.nextInt();
        } finally {
            scanner.nextLine();
        }
    }

    public String getString() {
        return scanner.nextLine();

    }
}
