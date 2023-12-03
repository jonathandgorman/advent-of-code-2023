package com.jonathandgorman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        var count = 0;
        try {
            var inputList = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/day1/src/main/resources/input.txt"));
            for (String entry : inputList) {
                var charStream = entry.chars().mapToObj(c -> (char) c);
                var digits = charStream.filter(Character::isDigit).map(String::valueOf).toList();

                int coordinate;
                var firstDigit = digits.getFirst();
                if (digits.size() > 1) {
                    var lastDigit = digits.getLast();
                    coordinate = Integer.parseInt(firstDigit + lastDigit);
                } else {
                    coordinate = Integer.parseInt(firstDigit + firstDigit);
                }

                count = count + coordinate;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Final total count is: " + count);
    }
}
