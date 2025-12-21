import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Agent1: Always Heal Strategy
 * This agent always plays H (heal) against all opponents.
 */
public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Initialization: Read player ID and initial opponent count
        int myId = Integer.parseInt(scanner.nextLine());
        int initialOpponentCount = Integer.parseInt(scanner.nextLine());
        // Game loop
        while (true) {
            // Read number of active opponents this turn
            int opponentCount = Integer.parseInt(scanner.nextLine());
            // Read opponent moves (always opponentCount lines: "opponent_id move")
            // Move is H, S, or N (N = no previous move on first turn)
            List<Integer> opponentIds = new ArrayList<>();
            for (int i = 0; i < opponentCount; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                int opponentId = Integer.parseInt(parts[0]);
                // char move = parts[1].charAt(0); // 'H', 'S', or 'N'
                opponentIds.add(opponentId);
            }
            // Output: Heal all opponents
            for (int opponentId : opponentIds) {
                System.out.println(opponentId + " H");
            }
        }
    }
}
