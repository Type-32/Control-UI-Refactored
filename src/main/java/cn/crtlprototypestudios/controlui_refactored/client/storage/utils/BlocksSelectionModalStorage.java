package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.ArrayList;

public class BlocksSelectionModalStorage {
    public static ArrayList<Block> blocksSelection = new ArrayList<>();
    public static ArrayList<Block> cachedBlocksRegistry = new ArrayList<>();
    public static ArrayList<String> cachedBlocksIdentifiers = new ArrayList<>();

    public static void refreshRegistry() {
        if (!cachedBlocksRegistry.isEmpty()) {
            return;
        }
        for(Block block : Registries.BLOCK.stream().toList()){
            if(block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR || block.asItem() == Items.AIR) continue;
            cachedBlocksRegistry.add(block);
            cachedBlocksIdentifiers.add(block.getLootTableId().toString());
        }
    }
}
