package org.minimalcode.convert;

import org.minimalcode.reflect.Property;

public interface ConversionManager {

    boolean canConvert(Class<?> sourceType, Class<?> targetType, Property sourceProperty, Property targetProperty);

    <T> Object convert(Object source, Class<T> targetType, Property sourceProperty, Property targetProperty);
}
