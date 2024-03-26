package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import baritone.api.command.datatypes.RelativeBlockPos;
import baritone.api.command.exception.CommandInvalidStateException;
import baritone.api.selection.ISelection;
import baritone.api.utils.BetterBlockPos;
import cn.crtlprototypestudios.controlui_refactored.client.BaritoneWrapper;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.SelectionArea;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.SelectionClipboard;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class SelectionStorage {
    public static ArrayList<SelectionArea> selections = new ArrayList<>();
    public static SelectionClipboard clipboard = null;

    public SelectionStorage(){
        selections = new ArrayList<>();
    }

    public static ArrayList<SelectionArea> fetchSelections(){
        selections.clear();
        for(ISelection sel : BaritoneWrapper.getInstance().getSelectionManager().getSelections()){
            selections.add(new SelectionArea(sel));
        }
        return selections;
    }

    public static SelectionClipboard pasteClipboardSelection(BlockPos pastePosition){
        if(SelectionStorage.clipboard == null) return null;

        SelectionClipboard clipboard = SelectionStorage.clipboard;
        SelectionStorage.clipboard = null;

        BetterBlockPos playerPos = BaritoneWrapper.getInstance().getPlayerContext().viewerPos();
        BetterBlockPos pos = pastePosition != null ? new BetterBlockPos(pastePosition) : playerPos;
        BaritoneWrapper.getInstance().getBuilderProcess().build("Fill", clipboard.getSchematic(), pos.offset(Direction.fromVector(clipboard.getOffset().getX(),clipboard.getOffset().getY(),clipboard.getOffset().getZ())));

        return clipboard;
    }
}
