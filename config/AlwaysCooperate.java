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

            List<Integer> opponentIds = new ArrayList<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] inputs = in.nextLine().split(" ");
                opponentIds.add(Integer.parseInt(inputs[0]));
            }

            for (int id : opponentIds) {
                System.out.println(id + " C");
            }
        }
    }
}
