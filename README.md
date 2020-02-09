[![Build Status](https://travis-ci.org/giova333/kalah-game.svg?branch=master)](https://travis-ci.org/giova333/kalah-game)
[![codecov.io](https://codecov.io/gh/giova333/kalah-game/branch/master/graphs/badge.svg?branch=master)](https://codecov.io/github/giova333/kalah-game?branch=master)

# Kalah
Java RESTful Web Service that runs a game of 6-stone Kalah.

#### Kalah Rules
Each of the two players has **six pits** in front of him/her. To the right of the six pits, each player has a larger pit, his
Kalah or house.
At the start of the game, six stones are put in each pit.
The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in
each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last
stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other
player's turn.
When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
other players' pit) and puts them in his own Kalah.
The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

#### Stack
* Java 8
* Spring Boot
* Lombok
* Commons Lang/Collections
* JUnit + Spring Boot Test
* Tomcat 8
* Docker
* Azure Kubernetes Service (AKS)


#### API

Web Service is deployed on Azure Kubernetes Service (AKS).

- Swagger API documentation: http://kalah-game.eastus.cloudapp.azure.com:8080/swagger-ui.html

- Create Game: 

```
curl --header "Content-Type: application/json" --request POST http://kalah-game.eastus.cloudapp.azure.com:8080/games
```

- Make a move:
```
curl --header "Content-Type: application/json" --request PUT http://kalah-game.eastus.cloudapp.azure.com:8080/games/{gameId}/pits/{pitId}
```

# How to run
If you want to run project on your local machine set "local" profile (Set environment variables = spring.profiles.active=local) and type the following command from the root directory:

```
mvn clean install
```