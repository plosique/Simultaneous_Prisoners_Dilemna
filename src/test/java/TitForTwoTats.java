import java.util.*;

/**
 * Tit for Two Tats: Cooperate unless the opponent defected
 * in both of the last two turns.
 */
public class TitForTwoTats {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int myId = Integer.parseInt(scanner.nextLine());
        int initialOpponentCount = Integer.parseInt(scanner.nextLine());
        Map<Integer, Character> prevOpponentMoves = new HashMap<>();

        while (true) {
            int opponentCount = Integer.parseInt(scanner.nextLine());
            Map<Integer, Character> currentMoves = new LinkedHashMap<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] parts = scanner.nextLine().split(" ");
                currentMoves.put(Integer.parseInt(parts[0]), parts[1].charAt(0));
            }
            for (Map.Entry<Integer, Character> entry : currentMoves.entrySet()) {
                int id = entry.getKey();
                char current = entry.getValue();
                char previous = prevOpponentMoves.getOrDefault(id, 'C');

                char myMove = (current == 'D' && previous == 'D') ? 'D' : 'C';
                System.out.println(id + " " + myMove);
            }
            prevOpponentMoves = new HashMap<>(currentMoves);
        }
    }
}
