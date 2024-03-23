package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus;

import cn.crtlprototypestudios.controlui_refactored.client.storage.types.SelectionArea;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.SelectionStorage;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.text.Text;

import java.util.Map;

public class ConstructionMenuScreen extends MenuScreen{
    public ConstructionMenuScreen() {
        super("menus/construction_menu", "Construction", true);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
    }
    @Override
    protected void init(){
        super.init();
        if(this.uiAdapter == null) return;

        this.uiAdapter.rootComponent.childById(FlowLayout.class, "selection-area-item-holder").<FlowLayout>configure(component -> {
            component.clearChildren();
            SelectionStorage.fetchSelections();
            if (SelectionStorage.selections == null) return;
            for (SelectionArea selectionArea : SelectionStorage.selections) {
                FlowLayout selectionAreaItem = this.model.expandTemplate(FlowLayout.class, "selection-area-item@controlui_refactored:components/selection_area_item", Map.of(
                        "corner-1-pos-x", String.valueOf(selectionArea.getPos1().getX()),
                        "corner-1-pos-y", String.valueOf(selectionArea.getPos1().getY()),
                        "corner-1-pos-z", String.valueOf(selectionArea.getPos1().getZ()),
                        "corner-2-pos-x", String.valueOf(selectionArea.getPos2().getX()),
                        "corner-2-pos-y", String.valueOf(selectionArea.getPos2().getY()),
                        "corner-2-pos-z", String.valueOf(selectionArea.getPos2().getZ()) // THis Mapping here does not work. I do not know why or how. It just malfunctions for some reason.
                ));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-1-pos-x").text(Text.of(String.valueOf(selectionArea.getPos1().getX())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-1-pos-y").text(Text.of(String.valueOf(selectionArea.getPos1().getY())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-1-pos-z").text(Text.of(String.valueOf(selectionArea.getPos1().getZ())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-2-pos-x").text(Text.of(String.valueOf(selectionArea.getPos2().getX())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-2-pos-y").text(Text.of(String.valueOf(selectionArea.getPos2().getY())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-2-pos-z").text(Text.of(String.valueOf(selectionArea.getPos2().getZ())));
                selectionAreaItem.childById(ItemComponent.class, "preview.corner-1-block-item").stack(selectionArea.getBlock1().asItem().getDefaultStack());
                selectionAreaItem.childById(ItemComponent.class, "preview.corner-2-block-item").stack(selectionArea.getBlock2().asItem().getDefaultStack());
                System.out.printf("%s %s %s", String.valueOf(selectionArea.getPos1().getX()), String.valueOf(selectionArea.getPos1().getY()), String.valueOf(selectionArea.getPos1().getZ()));
                component.child(selectionAreaItem);
            }
        });
    }
}
