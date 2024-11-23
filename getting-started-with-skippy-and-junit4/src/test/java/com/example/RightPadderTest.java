package com.example;

import io.skippy.junit4.Skippy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class RightPadderTest {

    @Rule
    public TestRule skippyRule = Skippy.predictWithSkippy();

    @Test
    public void testPadRight() {
        var input = TestConstants.HELLO;
        assertEquals("hello ", RightPadder.padRight(input, 6));
    }

}
