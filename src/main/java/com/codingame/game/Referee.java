package com.codingame.game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

import com.codingame.gameengine.module.entities.Circle;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;


    private Map<Integer, Map<Integer, Character>> previousMoves;
    private static final int MAX_TURNS = 100;
    private static final int INITIAL_MANA = 10000;

    private static final int CENTER_X = 960;   // Center of 1920 width
    private static final int CENTER_Y = 540;   // Center of 1080 height
    private static final int ORBIT_RADIUS = 350;  // Distance from center
    private static final int BASE_RADIUS = 40;    // Minimum player circle size
    private static final int RADIUS_PER_POINT = 5; // Extra radius per score point

    private static final int[] PLAYER_COLORS = {
        0xE63946, // Red
        0x2A9D84, // Teal
        0xF4A261, // Orange
        0x264653, //Dark blue
        0x8338EC, //Purple
        0x06D6A0, // Mint green
        0xFFBE0B, //Yellow
        0x3A86FF, // Blue
    };


    @Override
    public void init() {
        previousMoves = new HashMap<>();

        // Send initial game information to all players
        List<Player> players = gameManager.getActivePlayers();
        int opponentCount = players.size() - 1;

        for (Player player : players) {
            int playerId = player.getIndex();

            // Set active opponent count for getExpectedOutputLines()
            player.setActiveOpponentCount(opponentCount);

            // Send player their own ID
            player.sendInputLine(String.valueOf(playerId));

            // Send initial number of opponents
            player.sendInputLine(String.valueOf(opponentCount));

            //Set initial score 
            player.setScore(INITIAL_MANA);
            

            
        }
        initial_draw();
    }

    public void initial_draw(){
        List<Player> Players = gameManager.getActivePlayers();
        for(Player player: Players){

            graphicEntityModule
                    .createCircle()
                    .setRadius(70)
                    .setX(CENTER_X)
                    .setY(CENTER_Y)
                    .setLineWidth(0)
                    .setFillColor(player.getColorToken());
        }
    }

    @Override
    public void gameTurn(int turn) {
        List<Player> activePlayers = gameManager.getActivePlayers();

        // Step 1: Send input to all active players
        int opponentCount = activePlayers.size() - 1;
        for (Player player : activePlayers) {
            int playerId = player.getIndex();


            // Update player's active opponent count (needed for getExpectedOutputLines())
            player.setActiveOpponentCount(opponentCount);

            // Send number of active opponents for this turn
            player.sendInputLine(String.valueOf(opponentCount));

            // Send previous moves from other active players (one per line)
            if (!previousMoves.isEmpty() && previousMoves.containsKey(playerId)) {
                Map<Integer, Character> movesAgainstMe = previousMoves.get(playerId);
                for (Map.Entry<Integer, Character> entry : movesAgainstMe.entrySet()) {
                    player.sendInputLine(entry.getKey() + " " + entry.getValue());
                }
            } else {
                // First turn: send "opponent_id N" for each opponent
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
            
            try{
               Action action = player.getActions(player,activePlayers); 
               currentMoves.put(player.getIndex(), action.moves); 
               String summary = String.format("%s (id %d) played ", player.getNicknameToken(), player.getIndex());
               for(Map.Entry<Integer, Character> move : action.moves.entrySet()){
                summary+=move.getKey()+" "+move.getValue()+" ";
               }
               gameManager.addToGameSummary(summary);

               
            }catch(TimeoutException e){
                gameManager.addToGameSummary(gameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
                player.deactivate(player.getNicknameToken() + " timeout!");
                player.setScore(-1);
                endGame();
            
            }catch(InvalidAction e){
                gameManager.addToGameSummary(gameManager.formatErrorMessage(player.getNicknameToken() + " invalid output!"));
                player.deactivate(player.getNicknameToken() + " timeout!");
                player.setScore(-1);
                endGame();
            }

        }

        for (Player player : activePlayers) {
            int playerId = player.getIndex();

            Map<Integer, Character> playerMoves = currentMoves.get(playerId);
            int turnScore = 0;

            //calculate opponentScores
            for ( Character move : playerMoves.values()) {
                if (move == 'H'){
                    turnScore-=1;
                }else if(move == 'S'){
                    turnScore+=1;
                }
            }

            for(Map.Entry<Integer, Map<Integer,Character>> opponentChoices : currentMoves.entrySet()){
                int opponentId = opponentChoices.getKey(); 
                Map<Integer, Character> opponentMoves = opponentChoices.getValue(); 
                if(opponentId == playerId){
                    continue; 
                }
                Character opponentMove = opponentMoves.get(playerId); 
                if(opponentMove == 'H'){
                    turnScore+=1;

                }else if(opponentMove == 'S'){
                    turnScore-=1;
                }
            }

            player.setScore(player.getScore() + turnScore);
        }

        // Step 4: Store moves for next turn (reorganize by who received the move)
        Map<Integer, Map<Integer, Character>> movesReceivedByPlayer = new HashMap<>();
        for (Player player : activePlayers) {
            int playerId = player.getIndex();
            Map<Integer, Character> receivedMoves = new HashMap<>();

            for (Player opponent : activePlayers) {
                int opponentId = opponent.getIndex();
                if (playerId != opponentId && currentMoves.containsKey(opponentId)) {
                    // Player i receives the move that player j made against them
                    char move = currentMoves.get(opponentId).getOrDefault(playerId, 'D');
                    receivedMoves.put(opponentId, move);
                }
            }
            movesReceivedByPlayer.put(playerId, receivedMoves);
        }
        previousMoves = movesReceivedByPlayer;

        //show scores
        for(Player player : activePlayers){
            gameManager.addToGameSummary(String.format("%s (id %d) has %d mana",player.getNicknameToken(), player.getIndex(),player.getScore()));
        }

        // Check if game should end
        if (turn >= MAX_TURNS) {
            endGame();
        }
  }

  private void endGame(){
    int maxScore = 0;
    List<Player> activePlayers = gameManager.getActivePlayers(); 
    for( Player player: activePlayers){
         if(player.getScore() >= maxScore){
            maxScore = player.getScore();
         }
    }

    gameManager.endGame();

    //may be multiple winners
    for(Player player: activePlayers){
        if(player.getScore() == maxScore){
            gameManager.addToGameSummary(gameManager.formatSuccessMessage(player.getNicknameToken() + " won!"));
        }
    }
  }

}
