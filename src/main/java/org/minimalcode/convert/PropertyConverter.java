package org.minimalcode.convert;

import org.minimalcode.reflect.Property;

public interface PropertyConverter<S, T> {

    boolean canConvert(Property sourceProperty, Property targetProperty);

    T convert(S source, Property sourceProperty, Property targetProperty);
}
