import java.text.DecimalFormat;
import java.util.Scanner;

class GlassStatus {
    double heldVolume;

    GlassStatus() {
        this.heldVolume = 0;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true){
            int row;
            while (true) {
                System.out.println("Enter the number of the row from 2 to 50: ");
                row = sc.nextInt();
                if (row >= 2 && row <= 50) {
                    break;
                } else {
                    System.out.println("Invalid row. Please enter a number between 2 and 50.");
                }
            }

            int glass;
            while (true) {
                System.out.println("Enter the number of the glass in the row that you entered from 1 to " + row);
                glass = sc.nextInt();
                if (glass >= 1 && glass <= row) {
                    break;
                } else {
                    System.out.println("Invalid glass number in the row. Please enter a number between 1 and " + row + ".");
                }
            }

            // Calculate the time for the specified glass to start overflowing
            double time = computeOverflowTime(row, glass);
            DecimalFormat df = new DecimalFormat("#.###");
            System.out.println("The glass at row " + row + ", glass " + glass + " will start overflowing after " + df.format(time) + " seconds.");

            System.out.println("Do you want to continue? ([y] to continue, any other key to exit)");
            String response = sc.next();
            if (!response.equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    public static double computeOverflowTime(int targetRow, int targetGlass) {
        int totalRows = 50;
        GlassStatus[][] pyramid = new GlassStatus[totalRows][];

        // Initialize pyramid
        for (int i = 0; i < totalRows; i++) {
            pyramid[i] = new GlassStatus[i + 1];
            for (int j = 0; j <= i; j++) {
                pyramid[i][j] = new GlassStatus();
            }
        }

        // Start pouring water into the top glass
        double time = 0;
        double inflowRate = 0.1 / 1000;  // 0.1 units per second for a steady inflow, converted to milliseconds
        double timeStep = 0.001;  // 1 millisecond

        while (true) {
            boolean targetReached = false;
            for (int i = 0; i < totalRows; i++) {
                for (int j = 0; j <= i; j++) {
                    if (pyramid[i][j].heldVolume > 1.0) {
                        double overflow = pyramid[i][j].heldVolume - 1.0;
                        pyramid[i][j].heldVolume = 1.0;

                        if (i + 1 < totalRows) {
                            pyramid[i + 1][j].heldVolume += overflow / 2;
                            pyramid[i + 1][j + 1].heldVolume += overflow / 2;
                        }
                    }

                    if (i == targetRow - 1 && j == targetGlass - 1 && pyramid[i][j].heldVolume >= 1.0) {
                        targetReached = true;
                        break;
                    }
                }
                if (targetReached) break;
            }

            if (targetReached) break;

            // Pour water into the top glass for the next time unit
            pyramid[0][0].heldVolume += inflowRate;

            time += timeStep;  // Increment time by the time step
        }

        return time;
    }
}
