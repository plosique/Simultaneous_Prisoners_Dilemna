import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Map<Integer, Character> prevOpponentMoves = new HashMap<>();

        int myId = in.nextInt();
        int initialOpponentCount = in.nextInt();

        while (true) {
            int opponentCount = in.nextInt();
            if (in.hasNextLine()) {
                in.nextLine();
            }

            Map<Integer, Character> currentMoves = new LinkedHashMap<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] inputs = in.nextLine().split(" ");
                currentMoves.put(Integer.parseInt(inputs[0]), inputs[1].charAt(0));
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
