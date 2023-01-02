package com.sweater;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleTest {
    @Test
    public void test() {
        int x = 2;
        int y = 23;

        assertEquals(25, x+y);

        assertThat("mypwd", containsString("mypwd"));
    }

    @Test
    void error() {
        assertThrows(ArithmeticException.class, () -> {
            int a = 5/0;
        });
    }
}
