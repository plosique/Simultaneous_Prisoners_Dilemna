import java.util.*;

/**
 * Pavlov (Win-Stay, Lose-Shift): Start with C.
 * If both players played the same move last round, cooperate.
 * If they played different moves, defect.
 */
public class Pavlov {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int myId = Integer.parseInt(scanner.nextLine());
        int initialOpponentCount = Integer.parseInt(scanner.nextLine());
        Map<Integer, Character> myLastMoves = new HashMap<>();

        while (true) {
            int opponentCount = Integer.parseInt(scanner.nextLine());
            Map<Integer, Character> opponentMoves = new LinkedHashMap<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] parts = scanner.nextLine().split(" ");
                opponentMoves.put(Integer.parseInt(parts[0]), parts[1].charAt(0));
            }
            for (Map.Entry<Integer, Character> entry : opponentMoves.entrySet()) {
                int id = entry.getKey();
                char oppMove = entry.getValue();
                char myLast = myLastMoves.getOrDefault(id, 'C');

                char myMove;
                if (oppMove == 'N') {
                    myMove = 'C';
                } else if (myLast == oppMove) {
                    myMove = 'C';
                } else {
                    myMove = 'D';
                }

                myLastMoves.put(id, myMove);
                System.out.println(id + " " + myMove);
            }
        }
    }
}
