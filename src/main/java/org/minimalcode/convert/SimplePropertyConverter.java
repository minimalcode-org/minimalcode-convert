package org.minimalcode.convert;

import org.minimalcode.reflect.Property;

public abstract class SimplePropertyConverter<S, T> implements PropertyConverter<S, T> {
    @Override
    public boolean canConvert(Property sourceProperty, Property targetProperty) {
        return true;
    }

    @Override
    public T convert(S source, Property sourceProperty, Property targetProperty) {
        return convert(source);
    }

    public abstract T convert(S source);
}
