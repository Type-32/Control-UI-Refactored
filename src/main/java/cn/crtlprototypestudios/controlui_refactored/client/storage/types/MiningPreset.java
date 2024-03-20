package cn.crtlprototypestudios.controlui_refactored.client.storage.types;

import net.minecraft.block.Block;

import java.util.ArrayList;

public class MiningPreset {
    private String presetName;
    private String presetDescription;
    private MiningPresetType presetType;
    private ArrayList<Block> blocks;
    private int amount;

    public MiningPreset(String presetName, String presetDescription, MiningPresetType presetType, ArrayList<Block> blocks, int amount) {
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
        this.presetType = MiningPresetType.GoalBased;
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

    public MiningPresetType getPresetType() {
        return presetType;
    }

    public void setPresetType(MiningPresetType presetType) {
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
        return presetType == MiningPresetType.GoalBased ? String.format("mine %d %s", amount, blocks.get(0).getLootTableId().toString()) : String.format("mine %s", toIdentifierList(blocks));
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
            identifierList += block.getLootTableId().toString() + " ";
        }
        return identifierList;
    }
}
