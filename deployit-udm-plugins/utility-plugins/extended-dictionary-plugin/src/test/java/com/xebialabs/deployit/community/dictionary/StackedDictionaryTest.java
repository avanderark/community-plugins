package com.xebialabs.deployit.community.dictionary;

import com.google.common.collect.ImmutableMap;
import com.xebialabs.deployit.plugin.api.udm.Dictionary;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StackedDictionaryTest {

    private Dictionary d1;
    private Dictionary d2;
    private Dictionary d3;
    private Dictionary d4;
    private StackedDictionary hd;

    @Before
    public void setup() throws Exception {
        d1 = new Dictionary();
        d1.setEntries(ImmutableMap.of("X", "1", "Y", "1"));

        d2 = new Dictionary();
        d2.setEntries(ImmutableMap.of("X", "3", "Y", "3", "A", "8"));

        d3 = new Dictionary();
        d3.setEntries(ImmutableMap.of("X", "2"));

        d4 = new Dictionary();
        d4.setEntries(ImmutableMap.of("Y", "2"));

        hd = new StackedDictionary();

    }

    @Test
    public void testEntriesOnly() {
        hd.setEntries(ImmutableMap.of("foo", "bar"));
        final Map<String, String> entries = hd.getEntries();
        assertThat(entries.size(), is(1));
        assertTrue(entries.containsKey("foo"));
        assertThat(entries.get("foo"), is("bar"));
    }

    @Test
    public void testEmpty() {
        final Map<String, String> entries = hd.getEntries();
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testD1D2() {
        hd.setDictionaries(of(d1, d2));
        final Map<String, String> entries = hd.getEntries();
        assertThat(entries.size(), is(3));
        assertTrue(entries.containsKey("X"));
        assertTrue(entries.containsKey("Y"));
        assertThat(entries.get("X"), is("1"));
        assertThat(entries.get("Y"), is("1"));
        assertThat(entries.get("A"), is("8"));
    }

    @Test
    public void testD1D2Entries() {
        hd.setDictionaries(of(d1, d2));
        hd.setEntries(ImmutableMap.of("X", "4", "Y", "4", "A", "9"));
        final Map<String, String> entries = hd.getEntries();
        assertThat(entries.size(), is(3));
        assertTrue(entries.containsKey("X"));
        assertTrue(entries.containsKey("Y"));
        assertThat(entries.get("X"), is("4"));
        assertThat(entries.get("Y"), is("4"));
        assertThat(entries.get("A"), is("9"));
    }

    @Test
    public void testD2D1() {
        hd.setDictionaries(of(d2, d1));
        final Map<String, String> entries = hd.getEntries();
        assertThat(entries.size(), is(3));
        assertTrue(entries.containsKey("X"));
        assertTrue(entries.containsKey("Y"));
        assertThat(entries.get("X"), is("3"));
        assertThat(entries.get("Y"), is("3"));
    }

    @Test
    public void testD3D1() {
        hd.setDictionaries(of(d3, d1));
        final Map<String, String> entries = hd.getEntries();
        assertThat(entries.size(), is(2));
        assertTrue(entries.containsKey("X"));
        assertTrue(entries.containsKey("Y"));
        assertThat(entries.get("X"), is("2"));
        assertThat(entries.get("Y"), is("1"));
    }

    @Test
    public void testD4D1() {
        hd.setDictionaries(of(d4, d1));
        final Map<String, String> entries = hd.getEntries();
        assertThat(entries.size(), is(2));
        assertTrue(entries.containsKey("X"));
        assertTrue(entries.containsKey("Y"));
        assertThat(entries.get("X"), is("1"));
        assertThat(entries.get("Y"), is("2"));
    }

    @Test
    public void testD1D4() {
        hd.setDictionaries(of(d1, d4));
        final Map<String, String> entries = hd.getEntries();
        assertThat(entries.size(), is(2));
        assertTrue(entries.containsKey("X"));
        assertTrue(entries.containsKey("Y"));
        assertThat(entries.get("X"), is("1"));
        assertThat(entries.get("Y"), is("1"));
    }
}
