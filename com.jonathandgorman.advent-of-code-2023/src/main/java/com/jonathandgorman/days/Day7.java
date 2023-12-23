package com.jonathandgorman.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day7 {
    public enum HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD,
        UNKNOWN;
    }

    private static final List<HandType> HANDS_TYPE_BY_PRIORITY = List.of(
            HandType.FIVE_OF_A_KIND,
            HandType.FOUR_OF_A_KIND,
            HandType.FULL_HOUSE,
            HandType.THREE_OF_A_KIND,
            HandType.TWO_PAIR,
            HandType.ONE_PAIR,
            HandType.HIGH_CARD
    );

    private static final List<Character> cardValuesByPriority = Arrays.asList('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');

    public static void main(String[] args) throws IOException {

        record Hand(String cards, HandType type, int bid) implements Comparable<Hand> {
            @Override
            public int compareTo(Hand thatHand) {
                var theseChars = this.cards.chars().mapToObj(c -> (char) c).toList();
                var thoseChars = thatHand.cards.chars().mapToObj(c -> (char) c).toList();

                var comparison = 0;
                for (int i = 0; i < theseChars.size(); i++) {
                    var thisPriority = cardValuesByPriority.indexOf(theseChars.get(i));
                    var thatPriority = cardValuesByPriority.indexOf(thoseChars.get(i));

                    if (thisPriority > thatPriority) {
                        comparison = 1;
                        break;
                    } else if (thisPriority < thatPriority) {
                        comparison = -1;
                        break;
                    } // else we continue comparing cards
                }
                return comparison;
            }
        }

        var unorderedHands = new ArrayList<Hand>();
        var orderedHands = new ArrayList<Hand>();
        var inputRows = Files.readAllLines(Paths.get("com.jonathandgorman.advent-of-code-2023/src/main/resources/day7.txt"));

        for (String rows : inputRows) {
            String[] split = rows.split(" ");
            HandType type = determineType(split[0]);
            unorderedHands.add(new Hand(split[0], type, Integer.parseInt(split[1])));
        }

        // For each hand type, order the hands based on the second-ordering rule
        for (HandType handType : HANDS_TYPE_BY_PRIORITY) {
            var hands = unorderedHands.stream()
                    .filter(h -> h.type == handType)
                    .sorted()
                    .toList();

            orderedHands.addAll(hands);
        }

        var reverseOrderedHands = new ArrayList<Hand>();
        reverseOrderedHands = new ArrayList<>(orderedHands.reversed());

        var total = 0;
        for (int i = 0; i < reverseOrderedHands.size(); i++) {
            total = total + (reverseOrderedHands.get(i).bid * (i + 1));
        }

        System.out.printf("Total is %d%n", total);
    }

    private static HandType determineType(String cards) {
        var charMap = new HashMap<Character, Integer>();
        var chars = cards.chars().mapToObj(c -> (char) c).toList();

        for (Character character : chars) {
            charMap.merge(character, 1, Integer::sum); // merge duplicate Map keys using sum operation
        }

        var entries = charMap.entrySet();

        // determine the card type based on the number of sums per letter
        if (entries.size() == 5) return HandType.HIGH_CARD;
        if (entries.size() == 4) return HandType.ONE_PAIR;
        if (entries.size() == 3) {
            long countOfTwos = entries.stream().filter(e -> e.getValue() == 2).count();
            if (countOfTwos == 1) {
                return HandType.FULL_HOUSE;
            } else if (countOfTwos == 2) {
                return HandType.TWO_PAIR;
            } else {
                return HandType.THREE_OF_A_KIND;
            }
        }
        if (entries.size() == 2) {
            long countOfTwos = entries.stream().filter(e -> e.getValue() == 2).count();
            if (countOfTwos == 1) {
                return HandType.FULL_HOUSE;
            } else {
                return HandType.FOUR_OF_A_KIND;
            }
        }
        if (entries.size() == 1) {
            return HandType.FIVE_OF_A_KIND;
        }
        return HandType.UNKNOWN;
    }
}
