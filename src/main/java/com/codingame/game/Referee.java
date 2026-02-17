package com.codingame.game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.endscreen.EndScreenModule;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private EndScreenModule endScreenModule;

    private Map<Integer, Map<Integer, Character>> previousMoves;
    private static final int MAX_TURNS = 60;
    private static final int INITIAL_MANA = 0;

    // Graph layout
    private static final int GRAPH_LEFT = 120;
    private static final int GRAPH_RIGHT = 1250;
    private static final int GRAPH_TOP = 60;
    private static final int GRAPH_BOTTOM = 1000;

    // Leaderboard layout
    private static final int LB_LEFT = 1350;
    private static final int LB_TOP = 120;
    private static final int LB_ROW_HEIGHT = 90;

    // Graph scaling
    private int scoreMin;
    private int scoreMax;

    // Previous graph position per player [x, y]
    private Map<Integer, int[]> prevGraphPos;

    // Leaderboard UI elements per player
    private Map<Integer, Circle> lbIndicators;
    private Map<Integer, Text> lbNameTexts;
    private Map<Integer, Text> lbScoreTexts;

    @Override
    public void init() {
        previousMoves = new HashMap<>();

        List<Player> players = gameManager.getActivePlayers();
        int opponentCount = players.size() - 1;

        for (Player player : players) {
            int playerId = player.getIndex();
            player.setActiveOpponentCount(opponentCount);
            player.sendInputLine(String.valueOf(playerId));
            player.sendInputLine(String.valueOf(opponentCount));
            player.setScore(INITIAL_MANA);
        }

        computeScoreRange(players.size());
        initial_draw();
    }

    private void computeScoreRange(int numPlayers) {
        int maxPerTurn = 7 * (numPlayers - 1);
        scoreMin = 0;
        scoreMax = INITIAL_MANA + MAX_TURNS * maxPerTurn;
    }

    private int turnToX(int turn) {
        return GRAPH_LEFT + (int)((double) turn / MAX_TURNS * (GRAPH_RIGHT - GRAPH_LEFT));
    }

    private int scoreToY(int score) {
        double ratio = (double)(score - scoreMin) / (scoreMax - scoreMin);
        return GRAPH_BOTTOM - (int)(ratio * (GRAPH_BOTTOM - GRAPH_TOP));
    }

    // ── Graphics setup ──────────────────────────────────────────

    public void initial_draw() {
        List<Player> players = gameManager.getActivePlayers();
        prevGraphPos = new HashMap<>();

        drawGraphBackground();
        drawGraphAxes();
        drawGraphLabels();
        initLeaderboard(players);

        for (Player player : players) {
            int x = turnToX(0);
            int y = scoreToY(player.getScore());
            prevGraphPos.put(player.getIndex(), new int[]{x, y});
        }
    }

    private void drawGraphBackground() {
        graphicEntityModule.createRectangle()
            .setX(GRAPH_LEFT).setY(GRAPH_TOP)
            .setWidth(GRAPH_RIGHT - GRAPH_LEFT)
            .setHeight(GRAPH_BOTTOM - GRAPH_TOP)
            .setFillColor(0x1a1a2e)
            .setLineWidth(0);
    }

    private void drawGraphAxes() {
        graphicEntityModule.createLine()
            .setX(GRAPH_LEFT).setY(GRAPH_BOTTOM)
            .setX2(GRAPH_RIGHT).setY2(GRAPH_BOTTOM)
            .setLineWidth(2).setLineColor(0x888888);

        graphicEntityModule.createLine()
            .setX(GRAPH_LEFT).setY(GRAPH_TOP)
            .setX2(GRAPH_LEFT).setY2(GRAPH_BOTTOM)
            .setLineWidth(2).setLineColor(0x888888);
    }

    private void drawGraphLabels() {
        // X axis ticks
        for (int t = 0; t <= MAX_TURNS; t += 20) {
            int x = turnToX(t);
            graphicEntityModule.createText(String.valueOf(t))
                .setX(x).setY(GRAPH_BOTTOM + 10)
                .setAnchorX(0.5).setAnchorY(0)
                .setFontSize(20).setFillColor(0xAAAAAA);

            if (t > 0) {
                graphicEntityModule.createLine()
                    .setX(x).setY(GRAPH_TOP)
                    .setX2(x).setY2(GRAPH_BOTTOM)
                    .setLineWidth(1).setLineColor(0x2a2a3e);
            }
        }

        // Y axis ticks
        int range = scoreMax - scoreMin;
        int interval = computeNiceInterval(range, 6);
        int firstTick = ((scoreMin / interval) + 1) * interval;

        for (int score = firstTick; score <= scoreMax; score += interval) {
            int y = scoreToY(score);
            graphicEntityModule.createText(String.valueOf(score))
                .setX(GRAPH_LEFT - 10).setY(y)
                .setAnchorX(1).setAnchorY(0.5)
                .setFontSize(20).setFillColor(0xAAAAAA);

            graphicEntityModule.createLine()
                .setX(GRAPH_LEFT).setY(y)
                .setX2(GRAPH_RIGHT).setY2(y)
                .setLineWidth(1).setLineColor(0x2a2a3e);
        }
    }

    private int computeNiceInterval(int range, int targetTicks) {
        double rough = (double) range / targetTicks;
        double magnitude = Math.pow(10, Math.floor(Math.log10(rough)));
        double residual = rough / magnitude;
        double nice;
        if (residual <= 1.5) nice = 1;
        else if (residual <= 3.5) nice = 2;
        else if (residual <= 7.5) nice = 5;
        else nice = 10;
        return (int)(nice * magnitude);
    }

    private void initLeaderboard(List<Player> players) {
        lbIndicators = new HashMap<>();
        lbNameTexts = new HashMap<>();
        lbScoreTexts = new HashMap<>();

        graphicEntityModule.createText("Leaderboard")
            .setX(LB_LEFT).setY(LB_TOP - 60)
            .setAnchorX(0).setAnchorY(0)
            .setFontSize(32)
            .setFontWeight(Text.FontWeight.BOLD)
            .setFillColor(0xFFFFFF);

        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        for (int i = 0; i < sorted.size(); i++) {
            createLeaderboardRow(sorted.get(i), LB_TOP + i * LB_ROW_HEIGHT);
        }
    }

    private void createLeaderboardRow(Player player, int rowY) {
        Circle indicator = graphicEntityModule.createCircle()
            .setX(LB_LEFT).setY(rowY + 20)
            .setRadius(12)
            .setFillColor(player.getColorToken())
            .setLineWidth(0);
        lbIndicators.put(player.getIndex(), indicator);

        Text nameText = graphicEntityModule.createText(player.getNicknameToken())
            .setX(LB_LEFT + 25).setY(rowY + 5)
            .setAnchorX(0).setAnchorY(0)
            .setFontSize(26).setFillColor(0xFFFFFF);
        lbNameTexts.put(player.getIndex(), nameText);

        Text scoreText = graphicEntityModule.createText(String.valueOf(player.getScore()))
            .setX(LB_LEFT + 25).setY(rowY + 38)
            .setAnchorX(0).setAnchorY(0)
            .setFontSize(22).setFillColor(0xBBBBBB);
        lbScoreTexts.put(player.getIndex(), scoreText);
    }

    // ── Per-turn graphics ───────────────────────────────────────

    public void step_draw(int turn) {
        List<Player> activePlayers = gameManager.getActivePlayers();
        List<Player> allPlayers = gameManager.getPlayers();
        updateGraph(activePlayers, turn);
        updateLeaderboard(allPlayers);
    }

    private void updateGraph(List<Player> activePlayers, int turn) {
        int newX = turnToX(turn);
        for (Player player : activePlayers) {
            int newY = scoreToY(player.getScore());
            int[] prev = prevGraphPos.get(player.getIndex());

            graphicEntityModule.createLine()
                .setX(prev[0]).setY(prev[1])
                .setX2(newX).setY2(newY)
                .setLineWidth(4)
                .setLineColor(player.getColorToken());

            prevGraphPos.put(player.getIndex(), new int[]{newX, newY});
        }
    }

    private void updateLeaderboard(List<Player> activePlayers) {
        List<Player> sorted = new ArrayList<>(activePlayers);
        sorted.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        for (int i = 0; i < sorted.size(); i++) {
            Player player = sorted.get(i);
            int rowY = LB_TOP + i * LB_ROW_HEIGHT;

            lbIndicators.get(player.getIndex()).setY(rowY + 20);
            lbNameTexts.get(player.getIndex()).setY(rowY + 5);
            lbScoreTexts.get(player.getIndex())
                .setY(rowY + 38)
                .setText(String.valueOf(player.getScore()));
        }
    }

    // ── Game logic ──────────────────────────────────────────────

    @Override
    public void gameTurn(int turn) {
        List<Player> activePlayers = gameManager.getActivePlayers();

        int opponentCount = activePlayers.size() - 1;
        for (Player player : activePlayers) {
            int playerId = player.getIndex();

            player.setActiveOpponentCount(opponentCount);
            player.sendInputLine(String.valueOf(opponentCount));

            if (!previousMoves.isEmpty() && previousMoves.containsKey(playerId)) {
                Map<Integer, Character> movesAgainstMe = previousMoves.get(playerId);
                for (Map.Entry<Integer, Character> entry : movesAgainstMe.entrySet()) {
                    player.sendInputLine(entry.getKey() + " " + entry.getValue());
                }
            } else {
                for (Player opponent : activePlayers) {
                    if (opponent.getIndex() != playerId) {
                        player.sendInputLine(opponent.getIndex() + " N");
                    }
                }
            }
            player.execute();
        }

        Map<Integer, Map<Integer, Character>> currentMoves = new HashMap<>();

        for (Player player : activePlayers) {
            try {
                Action action = player.getActions(player, activePlayers);
                currentMoves.put(player.getIndex(), action.moves);
                String summary = String.format("%s (id %d) played ", player.getNicknameToken(), player.getIndex());
                for (Map.Entry<Integer, Character> move : action.moves.entrySet()) {
                    summary += move.getKey() + " " + move.getValue() + " ";
                }
                gameManager.addToGameSummary(summary);
            } catch (TimeoutException e) {
                gameManager.addToGameSummary(gameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
                player.deactivate(player.getNicknameToken() + " timeout!");
                player.setScore(-1);
            } catch (InvalidAction e) {
                gameManager.addToGameSummary(gameManager.formatErrorMessage(player.getNicknameToken() + " invalid output!"));
                player.deactivate(player.getNicknameToken() + " invalid output!");
                player.setScore(-1);
            }
        }

        // Get fresh list of active players after eliminations
        activePlayers = gameManager.getActivePlayers();

        // End game if fewer than 2 players remain
        if (activePlayers.size() < 2) {
            endGame();
            return;
        }

        for (Player player : activePlayers) {
            int playerId = player.getIndex();
            Map<Integer, Character> playerMoves = currentMoves.get(playerId);
            int turnScore = 0;

            for (Map.Entry<Integer, Map<Integer, Character>> opponentChoices : currentMoves.entrySet()) {
                int opponentId = opponentChoices.getKey();
                if (opponentId == playerId) continue;
                Map<Integer, Character> opponentMoves = opponentChoices.getValue();
                Character myMove = playerMoves.get(opponentId);
                Character opponentMove = opponentMoves.get(playerId);

                if (myMove == 'C' && opponentMove == 'C') {
                    turnScore += 4;
                } else if (myMove == 'C' && opponentMove == 'D') {
                    turnScore += 0;
                } else if (myMove == 'D' && opponentMove == 'C') {
                    turnScore += 7;
                } else if (myMove == 'D' && opponentMove == 'D') {
                    turnScore += 1;
                }
            }

            player.setScore(player.getScore() + turnScore);
        }

        Map<Integer, Map<Integer, Character>> movesReceivedByPlayer = new HashMap<>();
        for (Player player : activePlayers) {
            int playerId = player.getIndex();
            Map<Integer, Character> receivedMoves = new HashMap<>();

            for (Player opponent : activePlayers) {
                int opponentId = opponent.getIndex();
                if (playerId != opponentId && currentMoves.containsKey(opponentId)) {
                    char move = currentMoves.get(opponentId).getOrDefault(playerId, 'C');
                    receivedMoves.put(opponentId, move);
                }
            }
            movesReceivedByPlayer.put(playerId, receivedMoves);
        }
        previousMoves = movesReceivedByPlayer;

        for (Player player : activePlayers) {
            gameManager.addToGameSummary(String.format("%s (id %d) has %d mana",
                player.getNicknameToken(), player.getIndex(), player.getScore()));
        }

        step_draw(turn);

        if (turn >= MAX_TURNS) {
            endGame();
        }
    }

    // ── End game ────────────────────────────────────────────────

    private void endGame() {
        List<Player> activePlayers = gameManager.getActivePlayers();
        int maxScore = findMaxScore(activePlayers);

        gameManager.endGame();
        displayWinners(activePlayers, maxScore);
        generateEndScreen(activePlayers);
    }

    private int findMaxScore(List<Player> players) {
        int maxScore = 0;
        for (Player player : players) {
            if (player.getScore() >= maxScore) {
                maxScore = player.getScore();
            }
        }
        return maxScore;
    }

    private void displayWinners(List<Player> players, int maxScore) {
        for (Player player : players) {
            if (player.getScore() == maxScore) {
                gameManager.addToGameSummary(gameManager.formatSuccessMessage(player.getNicknameToken() + " won!"));
            }
        }
    }

    private void generateEndScreen(List<Player> players) {
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        int[] scores = new int[sortedPlayers.size()];
        String[] displayedText = new String[sortedPlayers.size()];

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            scores[i] = player.getScore();
            displayedText[i] = player.getScore() + " points";
        }

        endScreenModule.setScores(scores, displayedText);
    }
}
