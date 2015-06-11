package org.minimalcode.convert;

import org.minimalcode.reflect.Property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleConversionManager implements ConversionManager {

    private final Map<ConversionPair, PropertyConverter> converters = new ConcurrentHashMap<ConversionPair, PropertyConverter>();

    public void addConverter(Class<?> sourceType, Class<?> targetType, PropertyConverter converter) {
        assertNotNull(sourceType, "");
        assertNotNull(targetType, "");
        assertNotNull(converter, "");

        converters.put(new ConversionPair(sourceType, targetType), converter);
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType, Property sourceProperty, Property targetProperty) {
        assertNotNull(sourceType, "");
        assertNotNull(targetType, "");

        PropertyConverter converter = converters.get(new ConversionPair(sourceType, targetType));
        return (converter != null) && converter.canConvert(sourceProperty, targetProperty);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Object convert(Object source, Class<T> targetType, Property sourceProperty, Property targetProperty) {
        assertNotNull(source, "");
        assertNotNull(targetType, "");

        PropertyConverter converter = converters.get(new ConversionPair(source.getClass(), targetType));

        if(converter == null) {
            throw new IllegalArgumentException();
        }

        return converter.convert(source, sourceProperty, targetProperty);
    }

    private static void assertNotNull(Object obj, String message) {
        if(obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static class ConversionPair {
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConversionPair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ConversionPair other = (ConversionPair) o;
            return sourceType.equals(other.sourceType) && targetType.equals(other.targetType);
        }

        @Override
        public int hashCode() {
            return 31 * sourceType.hashCode() + targetType.hashCode();
        }
    }
}