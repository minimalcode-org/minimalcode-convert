package org.minimalcode.convert;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class SimpleConversionManagerTest {

    @Test
    public void testAddConverter() {

    }

    @Test
    public void testCanConvertSimple() {
        SimpleConversionManager manager = new SimpleConversionManager();
        assertFalse(manager.canConvert(String.class, Locale.class));

        manager.addConverter(new SimplePropertyConverter<String, Locale>() {
            @Override
            public Locale convert(String source) {
                return Locale.forLanguageTag(source);
            }
        });

        String str = "en";
        assertTrue(manager.canConvert(String.class, Locale.class));
        assertEquals(Locale.ENGLISH, manager.convert(str, Locale.class));
        assertNotEquals(Locale.ITALIAN, manager.convert(str, Locale.class));
    }

    @Test
    public void testCanConvert() {

    }


    @Test
    public void testConvertSimple() {

    }

    @Test
    public void testConvert() {

    }
}