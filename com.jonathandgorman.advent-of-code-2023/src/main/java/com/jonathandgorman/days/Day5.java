package com.jonathandgorman.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day5 {

    record Range(Long start, Long end) {}
    record RangeMapping(Range source, Range destination) {}

    static final List<String> MAPPING_SEQUENCE = List.of("seed-to-soil map:", "soil-to-fertilizer map:", "fertilizer-to-water map:", "water-to-light map:", "light-to-temperature map:", "temperature-to-humidity map:", "humidity-to-location map:");

    public static void main(String[] args) throws IOException {

        List<Long> seeds;
        ArrayList<List<RangeMapping>> rangeMappings = new ArrayList<>();
        var inputRows = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/src/main/resources/day5.txt"));

        seeds = Arrays.stream(inputRows.get(0).split(":")[1].trim().split(" "))
                .map(Long::valueOf)
                .toList();

        for (int i = 0; i < MAPPING_SEQUENCE.size(); i++) {
            var startIndex = inputRows.indexOf(MAPPING_SEQUENCE.get(i));
            List<String> targetList;
            if (i != MAPPING_SEQUENCE.size() - 1) {
                var endIndex = inputRows.indexOf(MAPPING_SEQUENCE.get(i + 1));
                targetList = inputRows.subList(startIndex + 1, endIndex - 1);
            } else {
                targetList = inputRows.subList(startIndex + 1, inputRows.size()); // handle last entry in mapping list
            }
            rangeMappings.add(generateRanges(targetList));
        }

        var lowestLocation = Long.MAX_VALUE;
        for (Long seed : seeds) {
            var input = seed;
            for (int i = 0; i < MAPPING_SEQUENCE.size(); i++) {
                var mappings = rangeMappings.get(i);

                Long finalInput = input;
                var rangeMatch = mappings.stream()
                        .filter(r -> finalInput >= r.source.start && finalInput <= r.source.end)
                        .toList();

                if (rangeMatch.size() > 1) {
                    throw new RuntimeException("Multiple matches encountered for input seed: " + seed);
                } else if (rangeMatch.size() == 1) {
                    var offset = input - rangeMatch.get(0).source.start;
                    input = rangeMatch.get(0).destination.start + offset;
                }

                // capture final location map value
                if (i == MAPPING_SEQUENCE.size() - 1) {
                    if (input < lowestLocation) {
                        lowestLocation = input;
                    }
                }
            }
        }
        System.out.println("The lowest location value found is " + lowestLocation);
    }

    private static List<RangeMapping> generateRanges(List<String> mappingRows) {

        List<RangeMapping> mapping = new ArrayList<>();

        for (String row : mappingRows) {
            if (!row.isBlank()) {

                var splitRow = row.split(" ");
                var destinationStart = Long.valueOf(splitRow[0]);
                var sourceStart = Long.valueOf(splitRow[1]);
                var range = Long.valueOf(splitRow[2]);

                mapping.add(new RangeMapping (
                                new Range(sourceStart, sourceStart + range - 1),
                                new Range(destinationStart, destinationStart + range - 1)
                        )
                );
            }
        }
        return mapping;
    }
}
