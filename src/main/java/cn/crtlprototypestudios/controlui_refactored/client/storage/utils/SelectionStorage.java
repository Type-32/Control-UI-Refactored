package cn.crtlprototypestudios.controlui_refactored.client.storage.utils;

import baritone.api.selection.ISelection;
import cn.crtlprototypestudios.controlui_refactored.client.BaritoneWrapper;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.SelectionArea;

import java.util.ArrayList;

public class SelectionStorage {
    public static ArrayList<SelectionArea> selections = new ArrayList<>();

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
}
