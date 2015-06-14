package org.minimalcode.convert;

import org.junit.Test;
import org.minimalcode.reflect.Bean;
import org.minimalcode.reflect.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class SimpleConversionManagerTest {

    static class Book {
        private List<String> titles;
        private List<String> upperCasedTitles;

        public List<String> getTitles() {
            return titles;
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        public List<String> getUpperCasedTitles() {
            return upperCasedTitles;
        }

        public void setUpperCasedTitles(List<String> upperCasedTitles) {
            this.upperCasedTitles = upperCasedTitles;
        }
    }

    @Test
    public void testAddConverter() {

    }

    @Test
    public void testAddConverterResolved() {

    }

    @Test
    public void testCanConvertSimple() {
        SimpleConversionManager manager = new SimpleConversionManager();
        manager.addConverter(new SimplePropertyConverter<String, Locale>() {
            @Override
            public Locale convert(String source) {
                return new Locale(source);
            }
        });

        String str = "en";
        assertTrue(manager.canConvert(String.class, Locale.class));
        assertEquals(Locale.ENGLISH, manager.convert(str, Locale.class));
        assertNotEquals(Locale.ITALIAN, manager.convert(str, Locale.class));
    }

    @Test
    public void testCanConvert() {
        SimpleConversionManager manager = new SimpleConversionManager();
        assertFalse(manager.canConvert(String.class, Locale.class));
    }


    @Test
    public void testConvertSimple() {

    }

    @Test
    public void testConvert() {
        SimpleConversionManager manager = new SimpleConversionManager();
        manager.addConverter(List.class, List.class, new PropertyConverter<List<String>, List<String>>() {
            @Override
            public boolean canConvert(Property sourceProperty, Property targetProperty) {
                return sourceProperty != null && sourceProperty.getActualType() == String.class
                        && targetProperty != null && targetProperty.getActualType() == String.class;
            }

            @Override
            public List<String> convert(List<String> source, Property sourceProperty, Property targetProperty) {
                List<String> upperCasedTitles = new ArrayList<String>(source.size());

                for (String title : source) {
                    upperCasedTitles.add(title.toUpperCase());
                }

                return upperCasedTitles;
            }
        });

        Bean<Book> bean = Bean.forClass(Book.class);
        Property titles = bean.getProperty("titles");
        Property upperCasedTitles = bean.getProperty("upperCasedTitles");

    }
}