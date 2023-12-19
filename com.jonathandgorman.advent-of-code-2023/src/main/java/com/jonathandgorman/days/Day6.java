package com.jonathandgorman.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Day6 {
    record BoatRace(int allowedTime, int distanceRecord) {}

    final static int NUMBER_OF_RACES = 4;

    public static void main(String[] args) throws IOException {

        var inputRows = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/src/main/resources/day6.txt"));
        var boatRaces = new ArrayList<BoatRace>(NUMBER_OF_RACES);

        int[] times = Arrays.stream(inputRows.getFirst().replaceAll("\\D+", " ").trim().split("\\s+"))
                .limit(NUMBER_OF_RACES)
                .mapToInt(Integer::parseInt)
                .toArray();

        int[] distances = Arrays.stream(inputRows.getLast().replaceAll("\\D+", " ").trim().split("\\s+"))
                .limit(NUMBER_OF_RACES)
                .mapToInt(Integer::parseInt)
                .toArray();

        // populate boat races
        for (int r = 0; r < NUMBER_OF_RACES; r++) {
            boatRaces.add(new BoatRace(times[r], distances[r]));
        }

        int[] waysToWin = {0, 0, 0, 0};
        var raceCount = 0;
        for (BoatRace race : boatRaces) {
            var winningRaces = 0;
            for (int option = 0; option <= race.allowedTime; option++) {
                var achievedDistance = option * (race.allowedTime - option);
                if (achievedDistance > race.distanceRecord) {
                    winningRaces++;
                }
            }
            waysToWin[raceCount] = winningRaces;
            raceCount++;
        }

        var marginOfError = Arrays.stream(waysToWin)
                .reduce(1, (a, b) -> a * b);

        System.out.println("The calculated margin of error is: " + marginOfError);
    }
}
