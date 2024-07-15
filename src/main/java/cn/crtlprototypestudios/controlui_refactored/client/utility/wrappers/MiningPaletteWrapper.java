package cn.crtlprototypestudios.controlui_refactored.client.utility.wrappers;

import cn.crtlprototypestudios.controlui_refactored.client.BaritoneWrapper;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;

public class MiningPaletteWrapper extends PaletteWrapperBase<MiningPreset> {
    public MiningPaletteWrapper(MiningPreset data) {
        super(data);
    }

    @Override
    public boolean execute() {
        BaritoneWrapper.getInstance().getCommandManager().execute(data.toCommand());
        return true;
    }
}
