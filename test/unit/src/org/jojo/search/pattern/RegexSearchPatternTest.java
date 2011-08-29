package org.jojo.search.pattern;

import org.jojo.search.FileEntry;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegexSearchPatternTest {

    public RegexSearchPatternTest() {
    }

    @Test
    public void testIsMatch() {
        RegexSearchPattern instance = new RegexSearchPattern();
        FileEntry fileEntry = new FileEntry(new File("/path/to/some.file"));

        assertFalse(instance.isMatch(null, null));
        assertFalse(instance.isMatch(fileEntry, null));
        assertFalse(instance.isMatch(null, "%some"));

        assertFalse(instance.isMatch(fileEntry, "%another.file"));
        assertFalse(instance.isMatch(fileEntry, "%some..file"));
        assertFalse(instance.isMatch(fileEntry, "%ath/t/o/some"));

        assertTrue(instance.isMatch(fileEntry, "%some.file"));
        assertTrue(instance.isMatch(fileEntry, "%SOMEFILE"));
        assertTrue(instance.isMatch(fileEntry, "%smefle"));
        assertTrue(instance.isMatch(fileEntry, "%pathtosomefile"));
    }

    @Test
    public void testIsValidQuery() {
        RegexSearchPattern instance = new RegexSearchPattern();

        assertFalse(instance.isValidQuery(null));
        assertFalse(instance.isValidQuery(""));
        assertFalse(instance.isValidQuery("ends with%"));

        assertTrue(instance.isValidQuery("%"));
        assertTrue(instance.isValidQuery("%starts with"));
    }
}
