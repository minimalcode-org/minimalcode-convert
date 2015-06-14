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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleConversionManager implements ConversionManager {

    private static final PropertyConverter<Object, Object> NOT_FOUND = new SimplePropertyConverter<Object, Object>() {
        @Override
        public Object convert(Object source) {
            return null;// never executed
        }
    };

    private final Map<Pair, PropertyConverter> convertersCache = new ConcurrentHashMap<Pair, PropertyConverter>();
    private final Map<Pair, LinkedList<PropertyConverter>> converters = new HashMap<Pair, LinkedList<PropertyConverter>>();

    public void addConverter(PropertyConverter<?, ?> converter) {
        try {
            Type[] types = ((ParameterizedType) converter.getClass().getGenericSuperclass()).getActualTypeArguments();
            addConverter((Class<?>) types[0], (Class<?>) types[1], converter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot resolve the generic types of " + converter + ". " +
                    "Use SimpleConversionManager::addConverter(Class<?>, Class<?>, PropertyConverter) instead.");
        }
    }

    public void addConverter(Class<?> sourceType, Class<?> targetType, PropertyConverter converter) {
        assertNotNull(sourceType, "Cannot add a converter with a 'null' sourceType.");
        assertNotNull(targetType, "Cannot add a converter with a 'null' targetType.");
        assertNotNull(converter, "Cannot add a 'null' converter.");

        Pair key = new Pair(sourceType, targetType, null, null);
        LinkedList<PropertyConverter> convertersForPair = converters.get(key);

        if(convertersForPair == null) {
            convertersForPair = new LinkedList<PropertyConverter>();
            converters.put(key, convertersForPair);
        }

        convertersCache.clear();// invalidate
        convertersForPair.addFirst(converter);
    }

    private PropertyConverter findConverter(Class<?> sourceType, Class<?> targetType, Property sourceProperty, Property targetProperty) {
        Pair key = new Pair(sourceType, targetType, sourceProperty, targetProperty);
        PropertyConverter converter = convertersCache.get(key);

        if(converter == null) {
            Pair keyWithoutProperties = new Pair(sourceType, targetType, null, null);
            List<PropertyConverter> convertersForPair = converters.get(keyWithoutProperties);

            if(convertersForPair != null) {
                for(PropertyConverter candidate : convertersForPair) {
                    if(candidate.canConvert(sourceProperty, targetProperty)) {
                        convertersCache.put(key, candidate);
                        return candidate;
                    }
                }
            }

            convertersCache.put(key, NOT_FOUND);
            return NOT_FOUND;
        }

        return converter;
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return canConvert(sourceType, targetType, null, null);
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType, Property sourceProperty, Property targetProperty) {
        assertNotNull(sourceType, "Cannot check a converter with a 'null' sourceType.");
        assertNotNull(targetType, "Cannot check a converter with a 'null' targetType.");

        return findConverter(sourceType, targetType, sourceProperty, targetProperty) != NOT_FOUND;
    }

    @Override
    public <T> Object convert(Object source, Class<T> targetType) {
        return convert(source, targetType, null, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Object convert(Object source, Class<T> targetType, Property sourceProperty, Property targetProperty) {
        assertNotNull(targetType, "Cannot convert with a 'null' targetType.");

        if(source == null) {
            return null;
        }

        PropertyConverter converter = findConverter(source.getClass(), targetType, sourceProperty, targetProperty);

        if(converter == NOT_FOUND) {
            throw new IllegalArgumentException("Cannot found a registred converter in the manager " +
                    "for source type '" + source.getClass().getName() + "' and target type '" + targetType.getName() +
                    "' and source property '" + sourceProperty + "' and target property '" + targetProperty + "'");
        }

        return converter.convert(source, sourceProperty, targetProperty);
    }

    private static void assertNotNull(Object obj, String message) {
        if(obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static class Pair {
        private final Class<?> sourceType;
        private final Class<?> targetType;
        private final Property sourceProperty;// Nullable
        private final Property targetProperty;// Nullable

        public Pair(Class<?> sourceType, Class<?> targetType, Property sourceProperty, Property targetProperty) {
            this.sourceType = sourceType;
            this.targetType = targetType;
            this.sourceProperty = sourceProperty;
            this.targetProperty = targetProperty;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Pair other = (Pair) obj;
            return sourceType.equals(other.sourceType) && targetType.equals(other.targetType)
                && !(sourceProperty != null ? !sourceProperty.equals(other.sourceProperty) : other.sourceProperty != null)
                && !(targetProperty != null ? !targetProperty.equals(other.targetProperty) : other.targetProperty != null);
        }

        @Override
        public int hashCode() {
            int result = sourceType.hashCode();
            result = 31 * result + targetType.hashCode();
            result = 31 * result + (sourceProperty != null ? sourceProperty.hashCode() : 0);
            result = 31 * result + (targetProperty != null ? targetProperty.hashCode() : 0);

            return result;
        }
    }
}
