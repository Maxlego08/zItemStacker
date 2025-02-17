package fr.maxlego08.zitemstacker.zcore.utils.map;

import java.util.Map;
import java.util.Optional;

public interface OptionalMap<K, V> extends Map<K, V> {

    /**
     * Returns an Optional containing the value associated with the given key,
     * or an empty Optional if the key is not present in the map.
     *
     * @param key the key whose associated value is to be returned
     * @return an Optional containing the value associated with the key, or an empty Optional if not present
     */
    Optional<V> getOptional(K key);

    /**
     * Checks if the map contains the given key.
     *
     * @param key the key whose presence in the map is to be tested
     * @return true if the map contains the key, false otherwise
     */
    boolean isPresent(K key);

}
