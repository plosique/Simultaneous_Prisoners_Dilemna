import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        int myId = in.nextInt();
        int initialOpponentCount = in.nextInt();

        while (true) {
            int opponentCount = in.nextInt();
            if (in.hasNextLine()) {
                in.nextLine();
            }

            Map<Integer, Character> opponentMoves = new LinkedHashMap<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] inputs = in.nextLine().split(" ");
                opponentMoves.put(Integer.parseInt(inputs[0]), inputs[1].charAt(0));
            }

            for (Map.Entry<Integer, Character> entry : opponentMoves.entrySet()) {
                char myMove = (entry.getValue() == 'D') ? 'D' : 'C';
                System.out.println(entry.getKey() + " " + myMove);
            }
        }
    }
}
