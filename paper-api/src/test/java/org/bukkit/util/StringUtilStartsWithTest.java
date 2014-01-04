package org.bukkit.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableList;

@RunWith(Parameterized.class)
public class StringUtilStartsWithTest {

    @Parameters(name= "{index}: {0} startsWith {1} == {2}")
    public static List<Object[]> data() {
        return ImmutableList.<Object[]>of(
            new Object[] {
                "Apple",
                "Apples",
                false
            },
            new Object[] {
                "Apples",
                "Apple",
                true
            },
            new Object[] {
                "Apple",
                "Apple",
                true
            },
            new Object[] {
                "Apple",
                "apples",
                false
            },
            new Object[] {
                "apple",
                "Apples",
                false
            },
            new Object[] {
                "apple",
                "apples",
                false
            },
            new Object[] {
                "Apples",
                "apPL",
                true
            },
            new Object[] {
                "123456789",
                "1234567",
                true
            },
            new Object[] {
                "",
                "",
                true
            },
            new Object[] {
                "string",
                "",
                true
            }
        );
    }

    @Parameter(0)
    public String base;
    @Parameter(1)
    public String prefix;
    @Parameter(2)
    public boolean result;

    @Test
    public void testFor() {
        assertThat(base + " starts with " + prefix + ": " + result, StringUtil.startsWithIgnoreCase(base, prefix), is(result));
    }
}
