package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.bukkit.util.ChatPaginator;
import org.junit.jupiter.api.Test;

public class ChatPaginatorTest {
    @Test
    public void testWordWrap1() {
        String rawString = ChatColor.RED + "123456789 123456789 123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 19);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is(ChatColor.RED + "123456789 123456789"));
        assertThat(lines[1], is(ChatColor.RED.toString() + "123456789"));
    }

    @Test
    public void testWordWrap2() {
        String rawString = "123456789 123456789 123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 22);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is("123456789 123456789"));
        assertThat(lines[1], is("123456789"));
    }

    @Test
    public void testWordWrap3() {
        String rawString = ChatColor.RED + "123456789 " + ChatColor.RED + ChatColor.RED + "123456789 " + ChatColor.RED + "123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 16);

        assertThat(lines.length, is(3));
        assertThat(lines[0], is(ChatColor.RED + "123456789"));
        assertThat(lines[1], is(ChatColor.RED.toString() + ChatColor.RED + ChatColor.RED + "123456789"));
        assertThat(lines[2], is(ChatColor.RED.toString() + ChatColor.RED + "123456789"));
    }

    @Test
    public void testWordWrap4() {
        String rawString = "123456789 123456789 123456789 12345";
        String[] lines = ChatPaginator.wordWrap(rawString, 19);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is("123456789 123456789"));
        assertThat(lines[1], is("123456789 12345"));
    }

    @Test
    public void testWordWrap5() {
        String rawString = "123456789\n123456789 123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 19);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is("123456789"));
        assertThat(lines[1], is("123456789 123456789"));
    }

    @Test
    public void testWordWrap6() {
        String rawString = "12345678   23456789 123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 19);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is("12345678   23456789"));
        assertThat(lines[1], is("123456789"));
    }

    @Test
    public void testWordWrap7() {
        String rawString = "12345678   23456789   123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 19);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is("12345678   23456789"));
        assertThat(lines[1], is("123456789"));
    }

    @Test
    public void testWordWrap8() {
        String rawString = "123456789 123456789 123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 6);

        assertThat(lines.length, is(6));
        assertThat(lines[0], is("123456"));
        assertThat(lines[1], is("789"));
        assertThat(lines[2], is("123456"));
        assertThat(lines[3], is("789"));
        assertThat(lines[4], is("123456"));
        assertThat(lines[5], is("789"));
    }

    @Test
    public void testWordWrap9() {
        String rawString = "1234 123456789 123456789 123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 6);

        assertThat(lines.length, is(7));
        assertThat(lines[0], is("1234"));
        assertThat(lines[1], is("123456"));
        assertThat(lines[2], is("789"));
        assertThat(lines[3], is("123456"));
        assertThat(lines[4], is("789"));
        assertThat(lines[5], is("123456"));
        assertThat(lines[6], is("789"));
    }

    @Test
    public void testWordWrap10() {
        String rawString = "123456789\n123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 19);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is("123456789"));
        assertThat(lines[1], is("123456789"));
    }

    @Test
    public void testWordWrap11() {
        String rawString = ChatColor.RED + "a a a " + ChatColor.BLUE + "a a";
        String[] lines = ChatPaginator.wordWrap(rawString, 9);

        assertThat(lines.length, is(1));
        assertThat(lines[0], is(ChatColor.RED + "a a a " + ChatColor.BLUE + "a a"));
    }

    @Test
    public void testWordWrap12() {
        String rawString = "123 1 123";
        String[] lines = ChatPaginator.wordWrap(rawString, 5);

        assertThat(lines.length, is(2));
        assertThat(lines[0], is("123 1"));
        assertThat(lines[1], is("123"));
    }

    @Test
    public void testWordWrap13() {
        String rawString = ChatColor.RED + "123456789 " + ChatColor.RED + ChatColor.BOLD + "123456789 " + ChatColor.RED + "123456789";
        String[] lines = ChatPaginator.wordWrap(rawString, 16);

        assertThat(lines.length, is(3));
        assertThat(lines[0], is(ChatColor.RED + "123456789"));
        assertThat(lines[1], is(ChatColor.RED.toString() + ChatColor.RED + ChatColor.BOLD + "123456789"));
        assertThat(lines[2], is(ChatColor.RED.toString() + ChatColor.BOLD + ChatColor.RED + "123456789"));
    }

    @Test
    public void testPaginate1() {
        String rawString = "1234 123456789 123456789 123456789";
        ChatPaginator.ChatPage page = ChatPaginator.paginate(rawString, 1, 6, 2);

        assertThat(page.getPageNumber(), is(1));
        assertThat(page.getTotalPages(), is(4));
        assertThat(page.getLines().length, is(2));
        assertThat(page.getLines()[0], is("1234"));
        assertThat(page.getLines()[1], is("123456"));
    }

    @Test
    public void testPaginate2() {
        String rawString = "1234 123456789 123456789 123456789";
        ChatPaginator.ChatPage page = ChatPaginator.paginate(rawString, 2, 6, 2);

        assertThat(page.getPageNumber(), is(2));
        assertThat(page.getTotalPages(), is(4));
        assertThat(page.getLines().length, is(2));
        assertThat(page.getLines()[0], is("789"));
        assertThat(page.getLines()[1], is("123456"));
    }

    @Test
    public void testPaginate3() {
        String rawString = "1234 123456789 123456789 123456789";
        ChatPaginator.ChatPage page = ChatPaginator.paginate(rawString, 4, 6, 2);

        assertThat(page.getPageNumber(), is(4));
        assertThat(page.getTotalPages(), is(4));
        assertThat(page.getLines().length, is(1));
        assertThat(page.getLines()[0], is("789"));
    }
}
