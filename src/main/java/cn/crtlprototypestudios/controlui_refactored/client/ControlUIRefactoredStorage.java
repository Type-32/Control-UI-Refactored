package cn.crtlprototypestudios.controlui_refactored.client;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.GsonBuilder;
import org.spongepowered.include.com.google.gson.JsonElement;
import org.spongepowered.include.com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ControlUIRefactoredStorage {
    private static final Path MOD_DATA_DIR = FabricLoader.getInstance().getGameDir()
            .resolve("controlui")
            .resolve("data");
    private static final Path MOD_CONFIG_DIR = FabricLoader.getInstance().getGameDir()
            .resolve("controlui")
            .resolve("config");

    private Path resolveDataFilePath(String filename) {
        return MOD_DATA_DIR.resolve(filename + ".json");
    }

    private Path resolveConfigFilePath(String filename) {
        return MOD_CONFIG_DIR.resolve(filename + ".json");
    }

    public void saveData(Object data, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path filePath = resolveDataFilePath(filename);
        try {
            Files.createDirectories(filePath.getParent());
            String json = gson.toJson((JsonElement) data);
            Files.writeString(filePath, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T loadData(Class<T> dataClass, String filename) {
        Gson gson = new Gson();
        Path filePath = resolveDataFilePath(filename);
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
}
