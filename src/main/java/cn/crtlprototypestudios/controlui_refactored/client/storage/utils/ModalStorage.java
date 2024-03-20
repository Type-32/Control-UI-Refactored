package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.block.Block;

public class ModalStorage {
    public static MiningPreset miningPreset;
    public static ArrayList<Block> blocksSelection = new ArrayList<>();
    public static ArrayList<Block> cachedBlocksRegistry = new ArrayList<>();

    public static void clearCache(){
        miningPreset = null;
        blocksSelection.clear();
    }

    public static void getAllRegisteredBlocks() {
        if (cachedBlocksRegistry.size() > 0) {
            return;
        }
        cachedBlocksRegistry.addAll(Registries.BLOCK.stream().toList());
    }
}
