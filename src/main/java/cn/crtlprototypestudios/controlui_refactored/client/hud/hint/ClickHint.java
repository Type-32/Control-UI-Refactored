package cn.crtlprototypestudios.controlui_refactored.client.hud.hint;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ClickHint extends BaseUIModelScreen<FlowLayout> {

    public ClickHint() {
        super(FlowLayout.class, new Identifier("controlui_refactored", "use_hint"));
    }

    @Override
    protected void build(FlowLayout rootComponent) {

    }
}
