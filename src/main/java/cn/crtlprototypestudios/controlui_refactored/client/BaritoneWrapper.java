package cn.crtlprototypestudios.controlui_refactored.client;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.process.IBaritoneProcess;


public class BaritoneWrapper {
    private static IBaritone instance;

    public static IBaritone getInstance() {
        if(instance == null){
            instance = BaritoneAPI.getProvider().getPrimaryBaritone();
        }
        return instance;
    }

    public static void stopAllActions(){
        // TODO: Custom Impl.
        getInstance().getPathingBehavior().cancelEverything();
//        getInstance().getPathingBehavior().cancelEverything();
//        getInstance().getMineProcess().cancel();
//        getInstance().getBuilderProcess().pause();
//        getInstance().getGetToBlockProcess().;
//        getInstance().getCustomGoalProcess().;
//        getInstance().getFollowProcess().cancel();
    }

    public static void pauseAllActions(){
        // This is currently the best impl. available because of the Baritone pause process is layered above every
        // other Baritone process, with a mutable and inaccessible boolean that controls the pause and resume state.
        // Due to this property a custom impl. of pausing baritone would be impossible and unnecessary, however this
        // makes lives harder as it will be hard to get the state of pause or resume of baritone.
        getInstance().getCommandManager().execute("pause");
    }

    public static void resumeAllActions(){
        // This is currently the best impl. somehow
        getInstance().getCommandManager().execute("resume");
    }

    public static IBaritoneProcess getCurrentTask(){
        return getInstance().getPathingControlManager().mostRecentInControl().orElse(null);
    }

    public static void getCurrentTaskETA(){
        // The code here is ripped straight off of the ETA command implemented in Baritone.

        IBaritoneProcess process = getCurrentTask();
        if (process == null) {
//            throw new CommandInvalidStateException("No process in control");
            System.out.println("Unable to retrieve ETA: No process in control");
        }
        IPathingBehavior pathingBehavior = getInstance().getPathingBehavior();

        double ticksRemainingInSegment = pathingBehavior.ticksRemainingInSegment().orElse(Double.NaN);
        double ticksRemainingInGoal = pathingBehavior.estimatedTicksToGoal().orElse(Double.NaN);

        System.out.println("Seconds remaining in segment: " + ticksRemainingInSegment / 20);
        System.out.println("Seconds remaining in goal: " + ticksRemainingInGoal / 20);
    }
}
