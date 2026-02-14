import com.codingame.gameengine.runner.MultiplayerGameRunner;

/**
 * LocalTestMain - For local development with web visualization
 * Run with: mvn exec:java -Dexec.mainClass="LocalTestMain"
 */
public class LocalTestMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Add test agents
        gameRunner.addAgent(AlwaysCooperate.class, "AlwaysCooperate");
        gameRunner.addAgent(AlwaysDefect.class, "AlwaysDefect");
        gameRunner.addAgent(TitForTat.class, "TitForTat");
        gameRunner.addAgent(RandomStrategy.class, "RandomStrategy");
        gameRunner.addAgent(Grudger.class, "Grudger");
        gameRunner.addAgent(Pavlov.class, "Pavlov");
        gameRunner.addAgent(TitForTwoTats.class, "TitForTwoTats");
        gameRunner.addAgent(SuspiciousTitForTat.class, "SuspiciousTitForTat");

        // Start with web server for visualization
        gameRunner.start(8888);
        System.out.println("\n===========================================");
        System.out.println("Game server started at http://localhost:8888");
        System.out.println("===========================================\n");
    }
}
