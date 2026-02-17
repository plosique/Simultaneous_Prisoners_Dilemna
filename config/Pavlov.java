import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Map<Integer, Character> myLastMoves = new HashMap<>();

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
