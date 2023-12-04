package com.jonathandgorman.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {
    public static void main(String[] args) throws IOException {

        var inputGames = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/src/main/resources/day2.txt"));
        var validGameIdSum = 0;

        for (String currentGame : inputGames) {
            Game game = parseGame(currentGame);
            if (game.id > 0) {
                var containsInvalidCombination = game.rounds.stream()
                        .flatMap(r -> r.combinations.stream().filter(combo -> !Day2.isValidCombination(combo)))
                        .findAny()
                        .isPresent();

                if (!containsInvalidCombination) {
                    validGameIdSum = validGameIdSum + game.id;
                }
            }
        }
        System.out.println("Final valid game ID count: " + validGameIdSum);
    }

    private static Boolean isValidCombination(CubeCombination combo) {
        switch (combo.colour) {
            case BLUE -> {
                return combo.times <= MAX_BLUE_CUBES;
            }
            case RED -> {
                return combo.times <= MAX_RED_CUBES;
            }
            case GREEN -> {
                return combo.times <= MAX_GREEN_CUBES;
            }
            case null, default -> {
                return false;
            }
        }
    }

    private static Game parseGame(String gameString) {
        var idString = Arrays.stream(gameString.split(":")).toList().get(0);
        var roundString = Arrays.stream(gameString.split(":")).toList().get(1);

        var id = idString
                .chars()
                .mapToObj(c -> (char) c)
                .filter(Character::isDigit)
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        var rounds = Arrays.stream(roundString.split(";"))
                .map(String::trim)
                .map(Day2::parseRound)
                .toList();

        return new Game(Integer.parseInt(id), rounds);
    }

    private static Round parseRound(String r) {
        var combinations = Arrays.stream(r.split(","))
                .map(entry -> entry.trim().split(" "))
                .map(tuple -> new CubeCombination(Integer.parseInt(tuple[0]), CubeColour.fromName(tuple[1])))
                .toList();

        return new Round(combinations);
    }


    private enum CubeColour {
        RED("red"),
        GREEN("green"),
        BLUE("blue");

        private final String name;

        CubeColour(String name) {
            this.name = name;
        }

        public static CubeColour fromName(String name) {
            for (CubeColour v : CubeColour.values()) {
                if (v.name.equalsIgnoreCase(name)) {
                    return v;
                }
            }
            return null;
        }
    }

    record CubeCombination(int times, CubeColour colour) {}
    record Round(List<CubeCombination> combinations) {}
    record Game(int id, List<Round> rounds) {}

    private static final int MAX_RED_CUBES = 12;
    private static final int MAX_GREEN_CUBES = 13;
    private static final int MAX_BLUE_CUBES = 14;
}
