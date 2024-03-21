package cn.crtlprototypestudios.controlui_refactored.client.storage.types;

import cn.crtlprototypestudios.controlui_refactored.client.data_types.IJsonConvertible;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class PresetsData implements IJsonConvertible<PresetsData> {
    public ArrayList<MiningPreset> miningPresets = new ArrayList<>();
    public PresetsData(){
        miningPresets = new ArrayList<>();
    }
    public PresetsData(ArrayList<MiningPreset> data) {
        miningPresets = data;
    }
    public PresetsData(JsonObject json){
        miningPresets = new ArrayList<>();
        PresetsData obj = fromJsonObject(json);
        miningPresets = new ArrayList<>(obj.miningPresets);
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        for(MiningPreset preset : miningPresets)
            array.add(preset.toJsonObject());
        json.add(JSON_MINING_PRESETS, array);
        return json;
    }

    @Override
    public PresetsData fromJsonObject(JsonObject json) {
        ArrayList<MiningPreset> temp = new ArrayList<>();
        for(JsonElement element : json.getAsJsonArray(JSON_MINING_PRESETS).asList()){
            temp.add(new MiningPreset(element.getAsJsonObject()));
        }
        return new PresetsData(temp);
    }

    private static final String JSON_MINING_PRESETS = "miningPresets";
}
