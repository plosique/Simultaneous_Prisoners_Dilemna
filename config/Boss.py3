import sys
import random

# Pick a strategy at random for this entire game
STRATEGIES = [
    "always_cooperate",
    "always_defect",
    "tit_for_tat",
    "suspicious_tit_for_tat",
    "tit_for_two_tats",
    "random",
    "grudger",
    "pavlov",
]
strategy = random.choice(STRATEGIES)

# State tracking
betrayers = set()
prev_opponent_moves = {}
my_last_moves = {}
first_turn = True

initial_opponent_count = int(input())

while True:
    opponent_count = int(input())

    opponents = []
    for i in range(opponent_count):
        opponent_id, move = input().split()
        opponent_id = int(opponent_id)
        opponents.append((opponent_id, move))

    for opponent_id, previous_move in opponents:
        if strategy == "always_cooperate":
            my_move = "C"

        elif strategy == "always_defect":
            my_move = "D"

        elif strategy == "tit_for_tat":
            my_move = "D" if previous_move == "D" else "C"

        elif strategy == "suspicious_tit_for_tat":
            if first_turn:
                my_move = "D"
            else:
                my_move = "D" if previous_move == "D" else "C"

        elif strategy == "tit_for_two_tats":
            prev = prev_opponent_moves.get(opponent_id, "C")
            my_move = "D" if (previous_move == "D" and prev == "D") else "C"

        elif strategy == "random":
            my_move = random.choice(["C", "D"])

        elif strategy == "grudger":
            if previous_move == "D":
                betrayers.add(opponent_id)
            my_move = "D" if opponent_id in betrayers else "C"

        elif strategy == "pavlov":
            my_last = my_last_moves.get(opponent_id, "C")
            if previous_move == "N":
                my_move = "C"
            elif my_last == previous_move:
                my_move = "C"
            else:
                my_move = "D"

        my_last_moves[opponent_id] = my_move
        print(f"{opponent_id} {my_move}")

    # Update state for next turn
    for opponent_id, move in opponents:
        prev_opponent_moves[opponent_id] = move
    first_turn = False
