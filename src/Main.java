import java.util.Scanner;

/**
 * The Main class represents the starting point for the quadratic equation solver application.
 * It prompts the user to choose between generating and solving roots for 30 or 3000 randomly generated sets of coefficients.
 * The user's choice determines the number of equations to be solved concurrently by the application.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        int choice;
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("Enter 1 to generate and solve for the roots for 30 randomly generated sets of coefficients.");
            System.out.println("Enter 2 to generate and solve for the roots for 3000 randomly generated sets of coefficients.");
            String input = scanner.nextLine();
            int inputInt = Integer.parseInt(input);
            try {
                if (inputInt == 1 || inputInt == 2) {
                    choice = inputInt;
                    break;
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            } catch (NumberFormatException e) {
                e.getCause();
            }

        }

        // Create and start the MasterThread based on the user's choice
        MasterThread masterThread = MasterThread.getMasterThread(choice);
        masterThread.start();

        scanner.close();
    }

}