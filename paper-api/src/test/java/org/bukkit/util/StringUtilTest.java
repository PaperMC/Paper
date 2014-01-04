package org.bukkit.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class StringUtilTest {

    @Test(expected=NullPointerException.class)
    public void nullPrefixTest() {
        StringUtil.startsWithIgnoreCase("String", null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullStringTest() {
        StringUtil.startsWithIgnoreCase(null, "String");
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullCollectionTest() {
        StringUtil.copyPartialMatches("Token", ImmutableList.<String>of(), null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullIterableTest() {
        StringUtil.copyPartialMatches("Token", null, new ArrayList<String>());
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullTokenTest() {
        StringUtil.copyPartialMatches(null, ImmutableList.<String>of(), new ArrayList<String>());
    }

    @Test
    public void copyTokenTest() {
        String token = "ab";
        Iterable<String> original = ImmutableList.of("ab12", "aC561", "AB5195", "Ab76", "", "a");
        List<String> expected =     ImmutableList.of("ab12",          "AB5195", "Ab76"         );
        List<String> list = new ArrayList<String>();
        assertThat(StringUtil.copyPartialMatches(token, original, list), is(expected));
        assertThat(StringUtil.copyPartialMatches(token, original, list), is(sameInstance(list)));
        assertThat(list.size(), is(expected.size() * 2));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void copyUnsupportedTest() {
        StringUtil.copyPartialMatches("token", ImmutableList.of("token1", "token2"), ImmutableList.of());
    }

    @Test(expected=IllegalArgumentException.class)
    public void copyNullTest() {
        StringUtil.copyPartialMatches("token", Arrays.asList("token1", "token2", null), new ArrayList<String>());
    }
}
