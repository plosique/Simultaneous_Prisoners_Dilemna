import com.codingame.gameengine.runner.MultiplayerGameRunner;

/**
 * LocalTestMain - For local development with web visualization
 * Run with: mvn exec:java -Dexec.mainClass="LocalTestMain"
 */
public class LocalTestMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Add test agents
        gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent(Agent2.class);

        // Start with web server for visualization
        gameRunner.start(8888);
        System.out.println("\n===========================================");
        System.out.println("Game server started at http://localhost:8888");
        System.out.println("===========================================\n");
    }
}
