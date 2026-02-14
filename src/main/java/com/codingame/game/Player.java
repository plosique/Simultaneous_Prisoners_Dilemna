package com.codingame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.Group;

// Uncomment the line below and comment the line under it to create a Solo Game
// public class Player extends AbstractSoloPlayer {
public class Player extends AbstractMultiplayerPlayer {
    private int activeOpponentCount;

    public void setActiveOpponentCount(int count) {
        this.activeOpponentCount = count;
    }

    public int getActiveOpponentCount() {
        return activeOpponentCount;
    }

    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player
        // Each player outputs one line per active opponent
        return activeOpponentCount;
    }
    public Action getActions(Player player, List<Player> activePlayers) throws InvalidAction, TimeoutException {
            List<String> outputs;
            try {
                outputs = player.getOutputs();
            } catch (TimeoutException e) {
                throw e;
            }

            int expectedOutputLines = activePlayers.size() - 1;
            int playerId = player.getIndex();

            Set<Integer> activePlayerIds = new HashSet<>();

            for(Player activePlayer: activePlayers){
                activePlayerIds.add(activePlayer.getIndex());
            }

            // Check if we have the right number of output lines
            if (outputs.size() != expectedOutputLines) {
                throw new InvalidAction(String.format("Expected %d output lines, got %d",
                        expectedOutputLines, outputs.size()));
            }

            Set<Integer> seenOpponents = new HashSet<>();
            Map<Integer, Character> moves = new HashMap<>();

            // Parse each output line: "opponent_id move"
            for (String line : outputs) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty()) {
                    throw new InvalidAction("Empty output line");
                }

                String[] tokens = trimmedLine.split("\\s+");

                // Each line should have exactly 2 tokens
                if (tokens.length != 2) {
                    throw new InvalidAction(String.format("Invalid format '%s' (expected 'player_id move')",
                            trimmedLine));
                }

                int opponentId;
                try {
                    opponentId = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidAction(String.format("Invalid player ID '%s' (not a number)",
                            tokens[0]));
                }

                // Check if opponent ID is self
                if (opponentId == playerId) {
                    throw new InvalidAction("Cannot play against yourself");
                }

                // Check if opponent ID is an active player
                if (!activePlayerIds.contains(opponentId)) {
                    throw new InvalidAction(String.format("Invalid opponent ID %d",
                            opponentId));
                }

                // Check for duplicate opponent
                if (seenOpponents.contains(opponentId)) {
                    throw new InvalidAction(String.format("Duplicate opponent ID %d",
                            opponentId));
                }
                seenOpponents.add(opponentId);

                // Validate move character
                String moveStr = tokens[1];
                if (moveStr.length() != 1 || (moveStr.charAt(0) != 'C' && moveStr.charAt(0) != 'D')) {
                    throw new InvalidAction(String.format("Invalid move '%s' (must be C or D)",
                            moveStr));
                }

                moves.put(opponentId, moveStr.charAt(0));
            }

            Action action = new Action(player, moves);

            return action;
    }
}
