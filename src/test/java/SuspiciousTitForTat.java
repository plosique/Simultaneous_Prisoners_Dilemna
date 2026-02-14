import java.util.*;

/**
 * Suspicious Tit for Tat: Like Tit for Tat but starts with D.
 * First turn: defect. Then mirror the opponent's last move.
 */
public class SuspiciousTitForTat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int myId = Integer.parseInt(scanner.nextLine());
        int initialOpponentCount = Integer.parseInt(scanner.nextLine());
        boolean firstTurn = true;

        while (true) {
            int opponentCount = Integer.parseInt(scanner.nextLine());
            Map<Integer, Character> opponentMoves = new LinkedHashMap<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] parts = scanner.nextLine().split(" ");
                opponentMoves.put(Integer.parseInt(parts[0]), parts[1].charAt(0));
            }
            for (Map.Entry<Integer, Character> entry : opponentMoves.entrySet()) {
                char myMove;
                if (firstTurn) {
                    myMove = 'D';
                } else {
                    myMove = (entry.getValue() == 'D') ? 'D' : 'C';
                }
                System.out.println(entry.getKey() + " " + myMove);
            }
            firstTurn = false;
        }
    }
}
