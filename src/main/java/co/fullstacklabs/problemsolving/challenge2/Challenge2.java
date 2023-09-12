package co.fullstacklabs.problemsolving.challenge2;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge2 {
    
    public static int diceFacesCalculator(int dice1, int dice2, int dice3) {
        boolean valid = List.of(dice1, dice2, dice3)
                .stream()
                .allMatch(d -> 1 <= d && d <= 6);
        if (!valid)
            throw new IllegalArgumentException();
        Map.Entry<Integer, List<Integer>> mostFrequent = List.of(dice1, dice2, dice3)
                .stream()
                .collect(groupingBy(i -> i))
                .entrySet()
                .stream()
                .max((d1, d2) ->  d1.getValue().size() != d2.getValue().size() ?d1.getValue().size() - d2.getValue().size():d1.getKey()-d2.getKey())
                .get();
        return mostFrequent.getKey()*mostFrequent.getValue().size();
    }
}
