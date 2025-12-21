# Iterated Prisoner's Dilemma - Game Implementation

## Game Overview
This is an implementation of the Iterated Prisoner's Dilemma tournament game for the CodinGame platform.

## Game Rules

### Basic Setup
- **Players**: Arbitrary number (2 or more)
- **Turns**: 100 turns per match
- **Moves**: Each turn, players output pairs of `(player_id, move)` for each active opponent
  - `C` = Cooperate
  - `D` = Defect

### Scoring (per pairwise interaction)
- Both cooperate (C vs C): **4 points** each
- Both defect (D vs D): **1 point** each
- Defector vs Cooperator (D vs C): **7 points** for defector, **0 points** for cooperator

Each player's score per turn = sum of payoffs against all active opponents.

### Input Format

**Initialization (once at start):**
- **Line 1:** Your player ID (integer)
- **Line 2:** Initial number of opponents (integer)

**Each Turn:**
- **Line 1:** Number of active opponents (integer)
- **Lines 2+:** One line per opponent in format `opponent_id move`
  - Move is `C` (cooperate), `D` (defect), or `N` (no previous move)
  - On the first turn, all moves are `N`
  - On subsequent turns, moves are `C` or `D`
  - Always exactly `opponent_count` lines
  - Only includes currently active (non-eliminated) players

**Example Initialization (3 players):**
```
0
2
```
This means: You are Player 0, there are initially 2 opponents.

**Example Turn 1:**
```
2
1 N
2 N
```
This means: There are 2 active opponents. Player 1 and Player 2 have no previous moves (first turn).

**Example Turn 2:**
```
2
1 C
2 D
```
This means: There are 2 active opponents. Player 1 cooperated with you, Player 2 defected against you.

### Output Format
**Multiple lines:** One line per active opponent
- Format per line: `opponent_id move`
- Each line contains the opponent's ID and your move against them (`C` or `D`)
- Must output exactly one line for each active opponent (excluding yourself)
- Order of lines doesn't matter

**Example (3 players):**
```
1 C
2 D
```
This means: Play C (cooperate) against Player 1, play D (defect) against Player 2.

### Elimination Rules
Players are eliminated if they:
- Provide wrong number of output lines (must equal number of active opponents)
- Provide empty output lines
- Use invalid format (each line must be "player_id move")
- Use invalid player IDs (non-existent or eliminated players)
- Try to play against themselves
- Include duplicate opponent IDs
- Use invalid move characters (anything other than 'C' or 'D')
- Timeout

**Eliminated players:**
- No longer receive input or participate in the game
- Are not included in other players' opponent lists
- Cannot score points

### Victory Condition
After 100 turns, the player with the highest total score wins.

## Implementation Details

### Key Files
- `src/main/java/com/codingame/game/Referee.java` - Main game logic
- `src/main/java/com/codingame/game/Player.java` - Player interface
- `src/test/java/SkeletonMain.java` - Test runner configuration
- `src/test/java/Agent1.java` & `Agent2.java` - Test agents

### Running the Game
```bash
mvn clean compile test-compile
mvn exec:java
```

Game visualization available at: http://localhost:8888/test.html

## Tournament Types (from rules.txt)
1. **Round-robin tournament**: Each strategy plays one match against every other strategy
2. **Evolutionary tournament**: Population evolves over 100 generations based on scores
