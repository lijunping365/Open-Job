/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.job.common.comparator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author 李俊平
 */
public class MapComparable {

    /**
     * <pr>
     * map 按照 key 排序
     * </pr>
     *
     * @param map
     * @param asc
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean asc) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> stream = map.entrySet().stream();
        if (asc) {
            stream.sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            stream.sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * <pr>
     * map 按照 value 排序
     * </pr>
     *
     * @param map
     * @param asc
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean asc) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> stream = map.entrySet().stream();
        if (asc) {
            //stream.sorted(Comparator.comparing(e -> e.getValue()))
            stream.sorted(Map.Entry.<K, V>comparingByValue())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            //stream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            stream.sorted(Map.Entry.<K, V>comparingByValue().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }
}
