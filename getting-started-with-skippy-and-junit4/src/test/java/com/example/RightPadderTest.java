package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PredictWithSkippy
public class RightPadderTest {

    @Test
    void testPadRight() {
        var input = TestConstants.HELLO;
        assertEquals("hello ", RightPadder.padRight(input, 6));
    }

}
