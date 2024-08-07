package cn.crtlprototypestudios.controlui_refactored.client.storage.types;

import cn.crtlprototypestudios.controlui_refactored.client.data_types.IJsonConvertible;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.BlocksSelectionModalStorage;
import cn.crtlprototypestudios.controlui_refactored.client.utility.interfaces.IPaletteExecutable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;

import java.util.ArrayList;

public class MiningPreset implements IJsonConvertible<MiningPreset> {
    private String presetName;
    private String presetDescription;
    private Types presetType;
    private ArrayList<Block> blocks;
    private int amount;

    public MiningPreset(String presetName, String presetDescription, Types presetType, ArrayList<Block> blocks, int amount) {
        this.blocks = blocks;
        this.amount = amount;
        this.presetName = presetName;
        this.presetDescription = presetDescription;
        this.presetType = presetType;
    }

    public MiningPreset(String presetName, String presetDescription){
        this.blocks = null;
        this.amount = 64;
        this.presetName = presetName;
        this.presetDescription = presetDescription;
        this.presetType = Types.GoalBased;
    }

    public MiningPreset(JsonObject json){
        MiningPreset obj = fromJsonObject(json);

        presetName = obj.getPresetName();
        presetDescription = obj.getPresetDescription();
        presetType = obj.getPresetType();
        blocks = obj.getBlocks();
        amount = obj.getAmount();
    }


    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public String getPresetDescription() {
        return presetDescription;
    }

    public void setPresetDescription(String presetDescription) {
        this.presetDescription = presetDescription;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Types getPresetType() {
        return presetType;
    }

    public void setPresetType(Types presetType) {
        this.presetType = presetType;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = new ArrayList<>(blocks);
    }

    /**
     * Converts this preset to a Baritone command.
     *
     * @return         Returns the command form of this preset.
     */
    public String toCommand(){
        return presetType == Types.GoalBased ? String.format("mine %d %s", amount, blocks.get(0).getLootTableId().toString()) : String.format("mine %s", toIdentifierList(blocks));
    }

    /**
     * Converts block lists to a string of identifier lists.
     *
     * @param  blocks    An ArrayList of Blocks.
     * @return          A string of block identifiers separated by spaces.
     */
    private String toIdentifierList(ArrayList<Block> blocks){
        String identifierList = "";
        for (Block block : blocks) {
            identifierList += block.getLootTableId().toString().replaceAll("blocks/", "") + " ";
        }
        return identifierList;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();
        JsonArray identifiers = new JsonArray();
        json.addProperty("presetName", presetName);
        json.addProperty("presetDescription", presetDescription);
        json.addProperty("presetType", presetType.toString());

        for(Block block : blocks)
            identifiers.add(block.getLootTableId().toString());
        json.add("blocks", identifiers);
        json.addProperty("amount", amount);
        return json;
    }

    @Override
    public MiningPreset fromJsonObject(JsonObject json) {
        ArrayList<Block> ids = new ArrayList<>();

        BlocksSelectionModalStorage.refreshRegistry();
        for(JsonElement id : json.getAsJsonArray(JSON_BLOCKS).asList()){
            if (BlocksSelectionModalStorage.cachedBlocksIdentifiers.contains(id.getAsString()))
                ids.add(BlocksSelectionModalStorage.cachedBlocksRegistry.get(BlocksSelectionModalStorage.cachedBlocksIdentifiers.indexOf(id.getAsString())));
        }
        return new MiningPreset(json.get(JSON_PRESET_NAME).getAsString(), json.get(JSON_PRESET_DESCRIPTION).getAsString(), Types.valueOf(json.get(JSON_PRESET_TYPE).getAsString()), ids, json.get(JSON_AMOUNT).getAsInt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiningPreset that = (MiningPreset) o;
        return amount == that.amount && presetName.equals(that.presetName) && presetDescription.equals(that.presetDescription) && presetType == that.presetType && blocks.equals(that.blocks);
    }

    private static final String JSON_PRESET_NAME = "presetName";
    private static final String JSON_PRESET_DESCRIPTION = "presetDescription";
    private static final String JSON_PRESET_TYPE = "presetType";
    private static final String JSON_BLOCKS = "blocks";
    private static final String JSON_AMOUNT = "amount";

    public enum Types {
        GoalBased,
        ArrayBased
    }
}
