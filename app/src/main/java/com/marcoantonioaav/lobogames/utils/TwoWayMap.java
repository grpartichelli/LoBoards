package com.marcoantonioaav.lobogames.utils;

import java.util.HashMap;
import java.util.Map;

public class TwoWayMap<K, V> {

    private final Map<K,V> forward = new HashMap<>();
    private final Map<V,K> backward = new HashMap<>();

    public synchronized void put(K key, V value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    public synchronized V getForward(K key) {
        return forward.get(key);
    }

    public synchronized K getBackward(V key) {
        return backward.get(key);
    }
}