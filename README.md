# Tic Tac Toe Implementation using RL

Tic tac toe implementation using RL espacially using expected SARSA algorithm.
I tested this app in Windows 10.
I used swing library in java standard library to visualize game board.

## Requirements

- maven
- jdk 1.8 or higher

## Build

1. Go to the root directory of this project.
2. Open terminal and type ```mvn package```. Make sure you have target directory in this project directory.
3. Type ```java -jar ./target/tic-tac-toe-1.0-SNAPSHOT.jar```
4. Make sure you can play game now.

## Training of RL Agents

If you want to train your agent from the begining, you can delete ```state_value.bin``` file, and play game.
You should do train agent many times to get better performance.