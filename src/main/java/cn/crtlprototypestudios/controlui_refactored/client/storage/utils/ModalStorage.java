package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPresetType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.ArrayList;

public class ModalStorage {
    public static MiningPreset miningPreset = new MiningPreset("", "", MiningPresetType.ArrayBased, new ArrayList<>(), 64);
    public static ArrayList<Block> blocksSelection = new ArrayList<>();
    public static ArrayList<Block> cachedBlocksRegistry = new ArrayList<>();

    public static void clearCache(){
        miningPreset = null;
        blocksSelection.clear();
    }

    public static void getAllRegisteredBlocks() {
        if (!cachedBlocksRegistry.isEmpty()) {
            return;
        }
        for(Block block : Registries.BLOCK.stream().toList()){
            if(block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR || block.asItem() == Items.AIR) continue;
            cachedBlocksRegistry.add(block);
        }
    }
}
