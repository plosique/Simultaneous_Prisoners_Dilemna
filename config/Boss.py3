import sys

# Read initialization input
my_id = int(input())
initial_opponent_count = int(input())

# Game loop
while True:
    # Read number of active opponents this turn
    opponent_count = int(input())

    # Read opponent moves from previous turn
    opponents = []
    for i in range(opponent_count):
        # opponent_id: ID of the opponent
        # move: 'C' (cooperate), 'D' (defect), or 'N' (no previous move on first turn)
        opponent_id, move = input().split()
        opponent_id = int(opponent_id)
        opponents.append((opponent_id, move))

    # Write your strategy here
    # Output one line per opponent: "opponent_id move"
    # where move is either 'C' (cooperate) or 'D' (defect)

    for opponent_id, previous_move in opponents:
        # Example: always cooperate
        print(f"{opponent_id} C")
