import java.util.*;

public class Grudger {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int myId = Integer.parseInt(scanner.nextLine());
        int initialOpponentCount = Integer.parseInt(scanner.nextLine());
        Set<Integer> betrayers = new HashSet<>();

        while (true) {
            int opponentCount = Integer.parseInt(scanner.nextLine());
            List<Integer> opponentIds = new ArrayList<>();
            for (int i = 0; i < opponentCount; i++) {
                String[] parts = scanner.nextLine().split(" ");
                int id = Integer.parseInt(parts[0]);
                if (parts[1].charAt(0) == 'D') betrayers.add(id);
                opponentIds.add(id);
            }
            for (int id : opponentIds) {
                System.out.println(id + " " + (betrayers.contains(id) ? "D" : "C"));
            }
        }
    }
}
