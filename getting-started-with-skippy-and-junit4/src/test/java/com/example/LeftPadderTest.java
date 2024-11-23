package com.example;

import io.skippy.junit4.Skippy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class LeftPadderTest {

    @Rule
    public TestRule skippyRule = Skippy.predictWithSkippy();

    @Test
    public void testPadLeft() {
        var input = TestConstants.HELLO;
        assertEquals(" hello", LeftPadder.padLeft(input, 6));
    }

}
