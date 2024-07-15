package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;

import java.util.ArrayList;

public class MiningModalStorage {
    public static MiningPreset miningPreset = new MiningPreset("", "", MiningPreset.Types.ArrayBased, new ArrayList<>(), 64);

    public static void clearCache(){
        miningPreset = null;
    }
}
