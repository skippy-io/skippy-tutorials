package com.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeftPadderTest {

    @Test
    public void testPadLeft() {
        var input = TestConstants.HELLO;
        assertEquals(" hello", LeftPadder.padLeft(input, 6));
    }

}
