package co.fullstacklabs.problemsolving;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import co.fullstacklabs.problemsolving.challenge2.Challenge2;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge2Test {

    @Test
    public void testCase1() {
        int value = Challenge2.diceFacesCalculator(6, 6, 6);
        assertEquals(18, value);
    }

    @Test
    public void testCase2() {
        int value = Challenge2.diceFacesCalculator(5, 5, 5);
        assertEquals(15, value);
    }

    @Test
    public void testCase3() {
        int value = Challenge2.diceFacesCalculator(1, 2, 3);
        assertEquals(3, value);
    }

    @Test
    public void testCase4() {
        int value = Challenge2.diceFacesCalculator(1, 3, 1);
        assertEquals(2, value);
    }

    @Test
    public void testCase5() {
        int value = Challenge2.diceFacesCalculator(3, 5, 3);
        assertEquals(6, value);
    }

    @Test
    public void testCase6() {
        int value = Challenge2.diceFacesCalculator(6, 5, 4);
        assertEquals(6, value);
    }

    @Test
    public void testCase7() {
        int value = Challenge2.diceFacesCalculator(4, 5, 6);
        assertEquals(6, value);
    }

    @Test
    public void testCase8() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Challenge2.diceFacesCalculator(7, 6, 5));

    }
    
    @Test
    public void testCase9() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Challenge2.diceFacesCalculator(0, 6, 5));

    }
    
    @Test
    public void testCase10() {
        Assertions.assertThrows(IllegalArgumentException.class, 
                                    () ->Challenge2.diceFacesCalculator(-1, 2, 3));
        
        
    }
}



