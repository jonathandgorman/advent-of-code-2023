package com.jonathandgorman.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Day3 {
    record Coordinate(int xCoordinate, int yCoordinate, char value) {
    }

    private static final List<Character> SYMBOLS = List.of('+', '-', '*', '@', '#', '$', '/', '&', '%', '=');

    public static void main(String[] args) throws IOException {

        var inputRows = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/src/main/resources/day3.txt"));
        List<List<Coordinate>> rows = new ArrayList<>();

        var yCoordinate = 0;
        for (String row : inputRows) {
            var rowChars = row.chars().mapToObj(s -> (char) s).toList();
            var rowCoordinates = new ArrayList<Coordinate>(rowChars.size());
            for (int xCoordinate = 0; xCoordinate < rowChars.size() - 1; xCoordinate++) {
                rowCoordinates.add(new Coordinate(xCoordinate, yCoordinate, rowChars.get(xCoordinate)));
            }
            rows.add(rowCoordinates);
        }

        var symbolCoordinates = rows.stream()
                .flatMap(Collection::stream)
                .filter(coordinate -> SYMBOLS.contains(coordinate.value))
                .toList();

        var partNumberSum = 0;
        for (Coordinate coordinate : symbolCoordinates) {
            Optional<Integer> partNumber = getAdjacentNumber(coordinate);
            if (partNumber.isPresent()) {
                partNumberSum = partNumberSum + partNumber.get();
            }
        }
    }

    private static Optional<Integer> getAdjacentNumber(Coordinate coordinate) {

        var adjacentCoordinates = generateAdjacentCoordinates(coordinate);

        for (Coordinate adjacentCoordinate : adjacentCoordinates) {

            if (!Character.isDigit(adjacentCoordinate.value)) {
                break; // adjacent coordinate not a number
            }

            for (int x = adjacentCoordinate.xCoordinate; x <= adjacentCoordinate.xCoordinate + 2; x++) {
                System.out.println(x);
            }
        }
        return null;
    }

    private static List<Coordinate> generateAdjacentCoordinates(Coordinate baseCoordinate) {
        return List.of(
                new Coordinate(baseCoordinate.xCoordinate, baseCoordinate.yCoordinate + 1, '.'), // North
                new Coordinate(baseCoordinate.xCoordinate + 1, baseCoordinate.yCoordinate + 1, '.'), // North-east
                new Coordinate(baseCoordinate.xCoordinate + 1, baseCoordinate.yCoordinate, '.'), // East
                new Coordinate(baseCoordinate.xCoordinate + 1, baseCoordinate.yCoordinate - 1, '.'), // South-east
                new Coordinate(baseCoordinate.xCoordinate, baseCoordinate.yCoordinate - 1, '.'), // South
                new Coordinate(baseCoordinate.xCoordinate - 1, baseCoordinate.yCoordinate - 1, '.'), // South-west
                new Coordinate(baseCoordinate.xCoordinate - 1, baseCoordinate.yCoordinate, '.'), // West
                new Coordinate(baseCoordinate.xCoordinate - 1, baseCoordinate.yCoordinate + 1, '.') // North-west
        );
    }
}
