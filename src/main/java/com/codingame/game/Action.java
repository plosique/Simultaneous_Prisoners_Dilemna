package com.codingame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Action {
    public Map<Integer,Character> moves;
    public Player player;
    
    public Action(Player player, Map<Integer, Character> moves) {
        this.player = player;
        this.moves = moves;
    }
    
    @Override
    public String toString() {
        String res = ""; 
        for(Map.Entry<Integer, Character> move: this.moves.entrySet()){
            res+=move.getKey() + " " + move.getValue();  
        }
        return res;
    }

}
