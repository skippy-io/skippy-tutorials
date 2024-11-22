package com.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RightPadderTest {

    @Test
    public void testPadRight() {
        var input = TestConstants.HELLO;
        assertEquals("hello ", RightPadder.padRight(input, 6));
    }

}
