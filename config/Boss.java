import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // Read initialization input
        int myId = in.nextInt();
        int initialOpponentCount = in.nextInt();

        // Game loop
        while (true) {
            // Read number of active opponents this turn
            int opponentCount = in.nextInt();
            if (in.hasNextLine()) {
                in.nextLine();
            }

            // Read opponent moves from previous turn
            List<int[]> opponents = new ArrayList<>();
            for (int i = 0; i < opponentCount; i++) {
                // opponentId: ID of the opponent
                // move: 'C' (cooperate), 'D' (defect), or 'N' (no previous move on first turn)
                String[] inputs = in.nextLine().split(" ");
                int opponentId = Integer.parseInt(inputs[0]);
                char move = inputs[1].charAt(0);
                opponents.add(new int[]{opponentId, move});
            }

            // Write your strategy here
            // Output one line per opponent: "opponent_id move"
            // where move is either 'C' (cooperate) or 'D' (defect)

            for (int[] opponent : opponents) {
                int opponentId = opponent[0];
                // Example: always cooperate
                System.out.println(opponentId + " C");
            }
        }
    }
}
