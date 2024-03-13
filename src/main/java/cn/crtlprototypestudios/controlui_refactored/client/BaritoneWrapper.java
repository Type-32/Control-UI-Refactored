package cn.crtlprototypestudios.controlui_refactored.client;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;


public class BaritoneWrapper {
    private static IBaritone instance;

    public static IBaritone getInstance() {
        if(instance == null){
            instance = BaritoneAPI.getProvider().getPrimaryBaritone();
        }
        return instance;
    }

    public static void stopAllActions(){
        getInstance().getCommandManager().execute("stop");
    }

    public static void pauseAllActions(){
        getInstance().getCommandManager().execute("pause");
    }

    public static void resumeAllActions(){
        getInstance().getCommandManager().execute("resume");
    }
}
