package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;

public class ModalStorage {
    public static MiningPreset miningPreset;

    public static void clearCache(){
        miningPreset = null;
    }
}
