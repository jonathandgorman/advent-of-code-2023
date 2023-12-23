package com.jonathandgorman.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {
    public static void main(String[] args) throws IOException {

        record CoordinateChoice(String leftChoice, String rightChoice) {
        }

        var inputRows = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/src/main/resources/day8.txt"));
        var instructions = inputRows.get(0).chars().mapToObj(c -> (char) c).toList();
        var networkMap = new HashMap<String, CoordinateChoice>();

        // Extract the numeric values from each row and add the key and value (L and R choice) to the network-map
        Pattern pattern = Pattern.compile("\\b[A-Z]{3}\\b");
        for (int i = 2; i < inputRows.size(); i++) {
            Matcher matcher = pattern.matcher(inputRows.get(i));
            var matches = new ArrayList<String>();
            while (matcher.find()) {
                String match = matcher.group();
                matches.add(match);
            }

            var key = matches.get(0);
            var value = new CoordinateChoice(matches.get(1), matches.get(2));
            networkMap.put(key, value);
        }

        // Increment through the instructions and take the L or R option respectively
        var nextPoint = "AAA";
        var lastPoint = "ZZZ";
        var attempts = 0;

        while (!nextPoint.equals(lastPoint)) {
            for (char instruction : instructions) {
                var choice = networkMap.get(nextPoint);
                if (instruction == 'R') {
                    nextPoint = choice.rightChoice;
                } else if (instruction == 'L') {
                    nextPoint = choice.leftChoice;
                } else {
                    throw new RuntimeException("Unknown instruction %s found".formatted(instruction));
                }
                attempts++;
            }
        }
        System.out.printf("Reached the last point after %d attempts%n", attempts);
    }
}
