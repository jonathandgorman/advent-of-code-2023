package com.jonathandgorman.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day4 {
    public static void main(String[] args) throws IOException {
        var totalPoints = 0;
        var inputCards = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/src/main/resources/day4.txt"));

        for (String card : inputCards) {
            var cardSplit = card.split(":")[1].split("\\|");
            var winningNumbers = Arrays.stream(cardSplit[0].trim().split(" ")).toList();
            var ticketNumbers = Arrays.stream(cardSplit[1].trim().split(" ")).filter(s -> !s.isBlank()).toList();

            var matchingNumbers = ticketNumbers.stream()
                    .distinct()
                    .filter(winningNumbers::contains)
                    .collect(Collectors.toSet());

            if (matchingNumbers.size() == 1) {
                totalPoints = totalPoints + 1;
            } else {
                var points = (int) Math.pow(2, matchingNumbers.size() - 1); // equivalent of doubling the value X times, where X = matchingNumbers.size() - 1 times
                totalPoints = totalPoints + points;
            }
        }
        System.out.println("Total points: " + totalPoints);
    }
}
