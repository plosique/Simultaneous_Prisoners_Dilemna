import java.util.*;

public class RandomStrategy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int myId = Integer.parseInt(scanner.nextLine());
        int initialOpponentCount = Integer.parseInt(scanner.nextLine());

        while (true) {
            int opponentCount = Integer.parseInt(scanner.nextLine());
            List<Integer> opponentIds = new ArrayList<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] parts = scanner.nextLine().split(" ");
                opponentIds.add(Integer.parseInt(parts[0]));
            }
            for (int id : opponentIds) {
                System.out.println(id + " " + (random.nextBoolean() ? "C" : "D"));
            }
        }
    }
}
