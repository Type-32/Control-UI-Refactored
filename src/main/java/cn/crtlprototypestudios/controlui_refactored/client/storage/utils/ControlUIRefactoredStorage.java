package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.GsonBuilder;
import org.spongepowered.include.com.google.gson.JsonElement;
import org.spongepowered.include.com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ControlUIRefactoredStorage {
    public static final Path MOD_DATA_DIR = FabricLoader.getInstance().getGameDir()
            .resolve("controlui")
            .resolve("data");
    public static final Path MOD_CONFIG_DIR = FabricLoader.getInstance().getGameDir()
            .resolve("controlui")
            .resolve("config");

    /**
     * Resolves the file path for data based on the filename and whether it's a relay file.
     *
     * @param filename the name of the file
     * @param relay    true if it's a relay file, false otherwise
     * @return the resolved path for the data file
     */
    @Contract(pure = true)
    private @NotNull Path resolveDataFilePath(String filename, boolean relay) {
        return MOD_DATA_DIR.resolve(filename + (relay ? ".temp.json" : ".json"));
    }

    /**
     * Resolves the file path for configuration based on the filename and whether it's a relay file.
     *
     * @param filename the name of the file
     * @param relay    true if it's a relay file, false otherwise
     * @return the resolved path for the configuration file
     */
    @Contract(pure = true)
    private @NotNull Path resolveConfigFilePath(String filename, boolean relay) {
        return MOD_CONFIG_DIR.resolve(filename + (relay ? ".temp.json" : ".json"));
    }

    /**
     * Saves the given data object to a JSON file.
     *
     * @param  data     the data object to be saved
     * @param  filename the name of the file to save the data to
     * @param  relay    a boolean flag indicating whether to relay the data
     */
    public void saveData(Object data, String filename, boolean relay) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path filePath = resolveDataFilePath(filename, relay);
        try {
            Files.createDirectories(filePath.getParent());
            String json = gson.toJson((JsonElement) data);
            Files.writeString(filePath, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the data of the specified class from a file with the given filename, optionally as a relay file.
     *
     * @param dataClass the class of the data to load
     * @param filename  the name of the file
     * @param relay     true if it's a relay file, false otherwise
     * @param <T>       the type of the data
     * @return the loaded data
     */
    public <T> T loadData(Class<T> dataClass, String filename, boolean relay) {
        Gson gson = new Gson();
        Path filePath = resolveDataFilePath(filename, relay);
        if (Files.exists(filePath)) {
            try {
                String json = Files.readString(filePath);
                return gson.fromJson(new JsonReader(Files.newBufferedReader(filePath)), dataClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return dataClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to create instance of data class", e);
        }
    }

    /**
     * Cleans up relay files in the data and configuration directories.
     */
    public void cleanRelay(){
        for(Path i : MOD_DATA_DIR){
            if(i.endsWith(".temp.json")) {
                try {
                    Files.delete(i);
                } catch (IOException e) {
                    System.out.println("[Control UI] [Warning] Unable to delete relay files... This may cause some data saving issues.");
                }
            }
        }
        for(Path i : MOD_CONFIG_DIR){
            if(i.endsWith(".temp.json")) {
                try {
                    Files.delete(i);
                } catch (IOException e) {
                    System.out.println("[Control UI] [Warning] Unable to delete relay files... This may cause some data saving issues.");
                }
            }
        }
    }
}
