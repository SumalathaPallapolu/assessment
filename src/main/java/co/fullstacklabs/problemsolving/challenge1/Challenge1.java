package co.fullstacklabs.problemsolving.challenge1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge1 {
    public static Map<String, Float> numbersFractionCalculator(Integer[] numbers) {
        Map<String, Float> results = new HashMap<String, Float>();
        results.put("positives", fraction(numbers, n -> n > 0));
        results.put("negative", fraction(numbers, n -> n < 0));
        results.put("zeros", fraction(numbers, n -> n == 0));
        return results;
    }

    private static float fraction(Integer[] numbers, Predicate<Integer> op) {
        return ((float) Arrays.stream(numbers).filter(op).count() / (float) numbers.length);
    }
}
