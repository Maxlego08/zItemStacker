package fr.maxlego08.zitemstacker.zcore.utils.map;

import java.util.HashMap;
import java.util.Optional;

public class OptionalHashMap<K, V> extends HashMap<K, V> implements OptionalMap<K, V>{

	/**
	 *
	 */
	private static final long serialVersionUID = -1389669310403530512L;

	/**
	 * Returns the value associated with the given key, or an empty Optional
	 * if the map does not contain the key.
	 *
	 * @param key
	 *            the key
	 * @return an Optional containing the value associated with the key, or
	 *         an empty Optional if the map does not contain the key
	 */
	public Optional<V> getOptional(K key) {
		V value = super.getOrDefault(key, null);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	/**
	 * Checks if the map contains the given key.
	 *
	 * @param key
	 *            the key
	 * @return true if the map contains the key, false otherwise
	 */
	public boolean isPresent(K key) {
		return getOptional(key).isPresent();
	}

}
