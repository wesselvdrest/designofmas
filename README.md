# Simulating Evolutionary Game Theory in a N-Player Population

_For this study we used the game Dots-and-Boxes._

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them. WARNING: Program runs on the Mac but visualization may be impaired in the case you run on a 7x7 grid.

```
- Update to Java Version 8 Update 221 (Using JDK 11 and 13 also works)
- A program like that runs Java files, like Eclipse

```
[Download latest version of Java here](https://www.java.com/en/download/) |
[Download latest version of Eclipse here](https://www.eclipse.org/downloads/)

### Installing

A step by step overview of how to get the development environment running.

The first step is to clone the file “group_B11_designofmas”

```
git clone https://github.com/wesselvdrest/group_B11_designofmas.git
```

Load the code into your Java IDE of choice. For example, in Eclipse 2019-09, go to "File" -> "Open projects from file system" and then select the Folder `group_B11_designofmas` which contains the (hidden) file `.project`.

For the tournament to run and write the results to the files, we'll need to allocate more memory to the thread stack size. To do this you open the run configurations and add `-Xss20m` to the VM arguments. This will set the thread stack size to 20MB, which will be sufficient to write the results of tournaments that will take a long time to finish.

```
Run
```

You know you can succesfully run the program in its current stage if you can select different playing strategies from 2 players. Then, let the games begin!

## Tournament
To follow how the population evolves, a tournament was designed. There are 50 agents that belong to 5 teams. Each team plays a different strategy, in which two agents from opposing teams play against each other in a one-on-one battle. The strategies are considered to be heuristics such as deciding randomly and with greed. Gradually, as the tournament progresses, the population will evolve. This occurs due to the losing agent taking over a winning agent’s strategy. The tournament will end with one of the following three states: one overall winning strategy, a close tie or continuously varying outcomes. Thus, the tournament is a way to facilitate the evolution of our population.

## File Details
In this section all files have been listed in alphabetical order and detailing which files are ours, which files are edited, and which files were taken from elsewhere and didn't change.

File Name |	Description
------------ | 	-------------
Agent.java |	Own file.
agents.csv |	Own file.
Board.java |	Taken from https://github.com/gaurav708/dots-and-boxes/blob/master/Board.java. Functions that have been added to the original: getAvailableMoves(ArrayList<Point> Boxes), amountBoxesLeft() and getChainInformation().
CsvParser.java |	Own file.
Edge.java |	Taken from https://github.com/gaurav708/dots-and-boxes/blob/master/Edge.java
GamePlay.java |	Taken from https://github.com/gaurav708/dots-and-boxes/blob/master/GamePlay.java. Altered the file in such a way that our design was used, and that the tournament could run correctly.
GameSolver.java |	Taken from https://github.com/gaurav708/dots-and-boxes/blob/master/GameSolver.java
Main.java |	Taken from https://github.com/gaurav708/dots-and-boxes/blob/master/Main.java. Altered the file in such a way that our design was used, and our options to start the game were implemented.
ReturnValues.java |	Own file.
SolverGreedy.java |	Own file.
SolverHeuristic.java |	Taken from https://github.com/gaurav708/dots-and-boxes/blob/master/GreedySolver.java
SolverRandom.java |	Own file.
SolverDoubleDealing.java |	Own file.
SolverGreedy.java |	Own file.
SolverShortestChain.java |	Own file.

We have also created the Results folder. All results of your run will be written to a text file, which can be found in the Results folder.

## Authors

* **Han Havinga** - *s2657139*
* **Julia Mol** - *s2904683*
* **Wessel van der Rest** - *s2873672*
* **Marc Tuinier** - *s2929139*

## Acknowledgments

*Original code from https://github.com/gaurav708/dots-and-boxes, this code provides the basic Dots and Boxes game, with structures like the board and turn taking conventions.Strategies of agents and the tournament setup are implemented by our own team. Only the SolverHeuristics is copied from the original code (file GreedySolver.java), since it was an interesting strategy to add to our own strategies


