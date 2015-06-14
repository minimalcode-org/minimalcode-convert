/*
 * Copyright 2015 Fabio Piro (minimalcode.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
