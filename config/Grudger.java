import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Set<Integer> betrayers = new HashSet<>();

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
                int id = Integer.parseInt(inputs[0]);
                if (inputs[1].charAt(0) == 'D') betrayers.add(id);
                opponentIds.add(id);
            }

            for (int id : opponentIds) {
                System.out.println(id + " " + (betrayers.contains(id) ? "D" : "C"));
            }
        }
    }
}
