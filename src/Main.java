import services.UserServices;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /* FINISH? */
        Scanner in = new Scanner(System.in);

        try {
            while (true) {
                System.out.print("Masukan Username: ");
                String username = in.nextLine();

                if (username.equalsIgnoreCase("exit")) break;

                UserServices services = new UserServices();
                services.activity(username);
            }

            System.out.println("Program Berhenti");
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

}
