import controllers.UserEvent;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        try {
            while (true) {
                System.out.print("Masukan Username: ");
                String username = in.nextLine();

                if (username.equalsIgnoreCase("exit")) break;

                UserEvent event = new UserEvent(username);
                event.getList();
            }

            System.out.println("Program Berhenti");
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

}
