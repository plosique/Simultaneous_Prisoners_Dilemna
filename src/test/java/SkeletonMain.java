import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        gameRunner.addAgent(AlwaysCooperate.class, "AlwaysCooperate");
        gameRunner.addAgent(AlwaysDefect.class, "AlwaysDefect");
        gameRunner.addAgent(TitForTat.class, "TitForTat");
        gameRunner.addAgent(RandomStrategy.class, "RandomStrategy");
        gameRunner.addAgent(Grudger.class, "Grudger");
        gameRunner.addAgent(Pavlov.class, "Pavlov");
        gameRunner.addAgent(TitForTwoTats.class, "TitForTwoTats");
        gameRunner.addAgent(SuspiciousTitForTat.class, "SuspiciousTitForTat");

        gameRunner.start();
    }
}
