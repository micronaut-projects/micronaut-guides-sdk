/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link GuideMerger} interface.
 */
@Singleton
public class DefaultGuideMerger implements GuideMerger {
    /**
     * Merges a list of guide metadata.
     *
     * @param metadatas The list of guide metadata to be merged.
     */
    @Override
    public <T extends Guide> void mergeGuides(List<T> metadatas) {
        Map<String, T> metadatasByDirectory = new TreeMap<>();
        for (T metadata : metadatas) {
            metadatasByDirectory.put(metadata.getSlug(), metadata);
        }

        mergeMetadataMap(metadatasByDirectory);

        metadatas.clear();
        metadatas.addAll(metadatasByDirectory.values());
    }

    private <T extends Guide> void mergeMetadataMap(Map<String, T> metadatasByDirectory) {
        List<String> dirs = new ArrayList<>(metadatasByDirectory.keySet());

        for (String dir : dirs) {
            T metadata = metadatasByDirectory.get(dir);
            if (metadata.getBase() != null) {
                T base = metadatasByDirectory.get(metadata.getBase());
                merge(base, metadata);
            }
        }
    }

    /**
     * Merges two guide metadata objects.
     *
     * @param base  The base guide metadata object.
     * @param guide The guide metadata object to merge into the base.
     * @param <T>   The type of the guide metadata objects.
     */
    public <T> void merge(T base, T guide) {
        try {
            for (Field field : guide.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                Object guideValue = field.get(guide);
                Object baseValue = field.get(base);

                if (guideValue instanceof List<?>) {
                    if (isOfType(field, App.class.getName())) {
                        List<App> mergedApps = mergeApps((List<App>) baseValue, (List<App>) guideValue);
                        field.set(guide, mergedApps);
                    } else {
                        List<?> mergedList = GuideMerger.mergeLists((List<?>) baseValue, (List<?>) guideValue);
                        field.set(guide, mergedList);
                    }
                } else if (guideValue == null) {
                    field.set(guide, baseValue);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error merging guides", e);
        }
    }

    private <T extends App> List<T> mergeApps(List<T> base, List<T> guide) {
        Map<String, T> baseApps = base.stream()
                .collect(Collectors.toMap(App::getName, app -> app));

        Map<String, T> guideApps = guide.stream()
                .collect(Collectors.toMap(App::getName, app -> app));

        Set<String> baseOnly = new HashSet<>(baseApps.keySet());
        baseOnly.removeAll(guideApps.keySet());

        Set<String> guideOnly = new HashSet<>(guideApps.keySet());
        guideOnly.removeAll(baseApps.keySet());

        Set<String> inBoth = new HashSet<>(baseApps.keySet());
        inBoth.retainAll(guideApps.keySet());

        List<T> merged = new ArrayList<>(baseOnly.stream()
                .map(baseApps::get)
                .toList());

        merged.addAll(guideOnly.stream()
                .map(guideApps::get)
                .toList());

        for (String name : inBoth) {
            T baseApp = baseApps.get(name);
            T guideApp = guideApps.get(name);
            merge(baseApp, guideApp);
            merged.add(guideApp);
        }

        return merged;
    }

    private boolean isOfType(Field field, String type) {
        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Type actualTypeArgument = ((ParameterizedType) stringListType).getActualTypeArguments()[0];

        if (actualTypeArgument instanceof Class<?>) {
            Class<?> stringListClass = (Class<?>) actualTypeArgument;
            if (stringListClass.getName().equals(type)) {
                return true;
            }
        } else if (actualTypeArgument instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) actualTypeArgument;
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length > 0 && upperBounds[0] instanceof Class<?>) {
                Class<?> boundClass = (Class<?>) upperBounds[0];
                if (boundClass.getName().equals(type)) {
                    return true;
                }
            }
        }

        return false;
    }
}
