//    Copyright (C) 2011  Ryan Michela
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.TestPlugin;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class MetadataConversionTest {
    private Plugin plugin = new TestPlugin("x");
    private FixedMetadataValue subject;

    private void setSubject(Object value) {
        subject = new FixedMetadataValue(plugin, value);
    }

    @Test
    public void testFromInt() {
        setSubject(10);

        assertEquals(10, subject.asInt());
        assertEquals(10, subject.asFloat(), 0.000001);
        assertEquals(10, subject.asDouble(), 0.000001);
        assertEquals(10, subject.asLong());
        assertEquals(10, subject.asShort());
        assertEquals(10, subject.asByte());
        assertEquals(true, subject.asBoolean());
        assertEquals("10", subject.asString());
    }

    @Test
    public void testFromFloat() {
        setSubject(10.5);

        assertEquals(10, subject.asInt());
        assertEquals(10.5, subject.asFloat(), 0.000001);
        assertEquals(10.5, subject.asDouble(), 0.000001);
        assertEquals(10, subject.asLong());
        assertEquals(10, subject.asShort());
        assertEquals(10, subject.asByte());
        assertEquals(true, subject.asBoolean());
        assertEquals("10.5", subject.asString());
    }

    @Test
    public void testFromNumericString() {
        setSubject("10");

        assertEquals(10, subject.asInt());
        assertEquals(10, subject.asFloat(), 0.000001);
        assertEquals(10, subject.asDouble(), 0.000001);
        assertEquals(10, subject.asLong());
        assertEquals(10, subject.asShort());
        assertEquals(10, subject.asByte());
        assertEquals(false, subject.asBoolean());
        assertEquals("10", subject.asString());
    }

    @Test
    public void testFromNonNumericString() {
        setSubject("true");

        assertEquals(0, subject.asInt());
        assertEquals(0, subject.asFloat(), 0.000001);
        assertEquals(0, subject.asDouble(), 0.000001);
        assertEquals(0, subject.asLong());
        assertEquals(0, subject.asShort());
        assertEquals(0, subject.asByte());
        assertEquals(true, subject.asBoolean());
        assertEquals("true", subject.asString());
    }

    @Test
    public void testFromNull() {
        setSubject(null);

        assertEquals(0, subject.asInt());
        assertEquals(0, subject.asFloat(), 0.000001);
        assertEquals(0, subject.asDouble(), 0.000001);
        assertEquals(0, subject.asLong());
        assertEquals(0, subject.asShort());
        assertEquals(0, subject.asByte());
        assertEquals(false, subject.asBoolean());
        assertEquals("", subject.asString());
    }
}
