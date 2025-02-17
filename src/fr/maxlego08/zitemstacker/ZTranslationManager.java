package fr.maxlego08.zitemstacker;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import fr.maxlego08.zitemstacker.api.TranslationManager;
import fr.maxlego08.zitemstacker.save.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZTranslationManager implements TranslationManager {

    private static final String JSON_URL = "https://raw.githubusercontent.com/Maxlego08/minecraft-assets/1.21/assets/minecraft/lang/_list.json";
    private static final String BASE_URL = "https://raw.githubusercontent.com/Maxlego08/minecraft-assets/1.21/assets/minecraft/lang/";
    private final ItemStackerPlugin plugin;
    private final Map<String, String> translationToKeys = new HashMap<>();
    private final Map<String, String> keyToMaterials = new HashMap<>();
    private final Map<String, String> keyToTranslations = new HashMap<>();
    private Map<Material, String> materialToKeys = new HashMap<>();

    public ZTranslationManager(ItemStackerPlugin plugin) {
        this.plugin = plugin;
    }

    public static List<String> findMatchingKeys(Map<String, String> map, String searchString) {
        List<String> matchingKeys = new ArrayList<>();
        for (String key : map.keySet()) {
            if (key.contains(searchString)) {
                matchingKeys.add(key);
            }
        }
        return matchingKeys;
    }

    @Override
    public void loadTranslations() {

        final File folder = new File(plugin.getDataFolder(), "langs");
        if (folder.exists()) {
            try {
                this.loadTranslation();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return;
        }

        folder.mkdirs();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                List<String> files = fetchFileList();
                downloadFiles(files, folder);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void loadTranslation() {

        if (Config.language == null) return;

        File file = new File(plugin.getDataFolder(), "langs/" + Config.language + ".json");
        if (!file.exists()) {
            plugin.getLogger().severe("File langs/" + Config.language + ".json doesnt exist ! ");
            return;
        }

        this.translationToKeys.clear();
        this.keyToMaterials.clear();
        this.keyToTranslations.clear();

        this.materialToKeys = getTranslationMaterials();
        this.materialToKeys.forEach((key, value) -> this.keyToMaterials.put(value, key.name()));

        Map<?, ?> map = plugin.getPersist().load(Map.class, file);
        map.forEach((k, v) -> {
            if (k instanceof String key && v instanceof String value) {
                if (key.startsWith("block") || key.startsWith("item")) {
                    translationToKeys.put(value.toLowerCase(), key);
                    keyToTranslations.put(key, value);
                }
            }
        });
    }

    private Map<Material, String> getTranslationMaterials() {
        Map<Material, String> values = new HashMap<>();
        for (Material value : Material.values()) {
            values.put(value, value.getTranslationKey());
        }
        return values;
    }

    private List<String> fetchFileList() throws IOException {
        URL url = URI.create(JSON_URL).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStreamReader reader = new InputStreamReader(new BufferedInputStream(connection.getInputStream()))) {
            Gson gson = new Gson();
            JsonData jsonData = gson.fromJson(reader, JsonData.class);
            return jsonData.getFiles();
        }
    }

    private void downloadFiles(List<String> files, File folder) {
        Path path = folder.toPath();
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }
        }

        for (String fileName : files) {
            String fileUrl = BASE_URL + fileName;
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                try {
                    downloadFile(fileUrl, Paths.get(folder.getAbsolutePath(), fileName.replace("_", "-")).toString());
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to download " + fileName + ": " + e.getMessage());
                }
            });
        }
    }

    private void downloadFile(String fileUrl, String destPath) throws IOException {
        URL url = URI.create(fileUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream()); FileOutputStream fileOutputStream = new FileOutputStream(destPath)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }

        if (Config.enableDebug) {
            this.plugin.getLogger().info("Download file " + fileUrl + " into " + destPath);
        }

        if (Config.language != null && destPath.contains(Config.language)) {
            this.loadTranslation();
        }
    }

    @Override
    public String replaceValue(String searchValue) {

        if (this.translationToKeys.containsKey(searchValue)) {
            String translationKey = this.translationToKeys.get(searchValue);
            if (translationKey != null && this.keyToMaterials.containsKey(translationKey)) {
                return this.keyToMaterials.get(translationKey);
            }
        }

        return searchValue;
    }

    @Override
    public String translateItemStack(ItemStack itemStack) {

        if (itemStack == null) return "ItemStack is NULL";

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }

        Material material = itemStack.getType();
        if (this.materialToKeys.containsKey(material)) {
            String key = this.materialToKeys.get(material);
            if (key != null && this.keyToTranslations.containsKey(key)) {
                return this.keyToTranslations.get(key);
            }
        }

        String name = material.name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    static class JsonData {
        @SerializedName("files")
        private List<String> files;

        public List<String> getFiles() {
            return files;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }
    }

}
