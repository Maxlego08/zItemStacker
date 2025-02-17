package fr.maxlego08.zitemstacker.zcore.utils.loader;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Interface that allows to load and save objects from/to a {@link YamlConfiguration}
 *
 * @param <T> the type of the object to load/save
 * @author Maxlego08
 */
public interface Loader<T> {

    /**
     * Load an object from a yml configuration
     *
     * @param configuration the yml configuration
     * @param path          the path to the object
     * @return the loaded object
     */
    T load(YamlConfiguration configuration, String path);

    /**
     * Save an object to a yml configuration
     *
     * @param object        the object to save
     * @param configuration the yml configuration
     * @param path          the path to the object
     */
    void save(T object, YamlConfiguration configuration, String path);

}
