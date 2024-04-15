package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus;

import cn.crtlprototypestudios.controlui_refactored.client.BaritoneWrapper;
import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals.BlocksSelectionModalScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.SelectionArea;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.SelectionStorage;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
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
        InitializeSelections();
    }

    private void InitializeSelections(){
        this.uiAdapter.rootComponent.childById(FlowLayout.class, "selection-area-item-holder").<FlowLayout>configure(component -> {
            component.clearChildren();
            SelectionStorage.fetchSelections();
            if (SelectionStorage.selections == null) return;
            for (SelectionArea selectionArea : SelectionStorage.selections) {
                FlowLayout selectionAreaItem = this.model.expandTemplate(FlowLayout.class, "selection-area-item@controlui_refactored:components/selection_area_item", Map.of()); // The value mapping here does not work. I do not know why or how. It just malfunctions for some reason.

                selectionAreaItem.childById(LabelComponent.class, "preview.corner-1-pos-x").text(Text.of(String.valueOf(selectionArea.getPos1().getX())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-1-pos-y").text(Text.of(String.valueOf(selectionArea.getPos1().getY())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-1-pos-z").text(Text.of(String.valueOf(selectionArea.getPos1().getZ())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-2-pos-x").text(Text.of(String.valueOf(selectionArea.getPos2().getX())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-2-pos-y").text(Text.of(String.valueOf(selectionArea.getPos2().getY())));
                selectionAreaItem.childById(LabelComponent.class, "preview.corner-2-pos-z").text(Text.of(String.valueOf(selectionArea.getPos2().getZ())));
                selectionAreaItem.childById(ItemComponent.class, "preview.corner-1-block-item").stack(selectionArea.getBlock1().asItem().getDefaultStack());
                selectionAreaItem.childById(ItemComponent.class, "preview.corner-2-block-item").stack(selectionArea.getBlock2().asItem().getDefaultStack());

                selectionAreaItem.childById(ButtonComponent.class, "action.delete-selection").onPress(component1 -> {
                    BaritoneWrapper.getInstance().getSelectionManager().removeSelection(selectionArea.getFromBaritone());
                    SelectionStorage.selections.remove(selectionArea);
                    SelectionStorage.fetchSelections();
                    InitializeSelections();
                });

                List<Component> childComponents = new ArrayList<>(selectionAreaItem.children());
                selectionAreaItem.childById(ButtonComponent.class, "action.more-actions").onPress(component1 -> { // The More Actions Button, after pressing it shows the tool menu at the slot
                    selectionAreaItem.clearChildren();

                    FlowLayout moreActionsSection = this.model.expandTemplate(FlowLayout.class, "selection-area-item-choices@controlui_refactored:components/selection_area_item", Map.of());
                    moreActionsSection.childById(ButtonComponent.class, "operation.fill").onPress(component2 -> { // The Fill button
                        BlocksSelectionModalScreen modal = new BlocksSelectionModalScreen(false, null,
                                result -> {
                                    if(result != null) {
                                        selectionArea.fillArea(result.get(0));
                                        ScreenStackUtils.exit();
                                    }else{
                                        ScreenStackUtils.back();
                                    }
                                }
                        );
                        ScreenStackUtils.to(modal);
                    });
                    moreActionsSection.childById(ButtonComponent.class, "operation.replace").onPress(component2 -> { // The Fill button
                        BlocksSelectionModalScreen modal = new BlocksSelectionModalScreen(false, null,
                                result -> {
                                    if(result != null) { // First Modal Layer checks for player selecting the replacing block.
                                        BlocksSelectionModalScreen multiSelectModal = new BlocksSelectionModalScreen(true, null, multiResult -> {
                                            if(multiResult != null){ // Second Modal Layer checks for player's about-to-be-replaced blocks.
                                                selectionArea.replaceArea(result.get(0), multiResult);
                                                ScreenStackUtils.exit();
                                            }else{
                                                ScreenStackUtils.back();
                                            }
                                        }, "Select Blocks to Replace");
                                        ScreenStackUtils.to(multiSelectModal);
                                    }else{
                                        ScreenStackUtils.back();
                                    }
                                }, "Select Replacing Block"
                        );
                        ScreenStackUtils.to(modal);
                    });
                    moreActionsSection.childById(ButtonComponent.class, "operation.clear").onPress(component2 -> {
                        selectionArea.clearArea();
                        ScreenStackUtils.exit();
                    });
                    moreActionsSection.childById(ButtonComponent.class, "operation.copy").onPress(component2 -> {
                        // TODO Impl. copy action here
                        selectionArea.copyArea(null);
                    });
                    moreActionsSection.childById(ButtonComponent.class, "operation.edit").onPress(component2 -> {
                        // TODO area selection edit impl.
                        // TODO Open modal to edit coords editing + macro run, i.e. copy / paste / expand / contract etc.

                        ScreenStackUtils.exit();
                    });
                    moreActionsSection.childById(ButtonComponent.class, "operation.back").onPress(component2 -> {
                        selectionAreaItem.clearChildren();
                        selectionAreaItem.children(childComponents);
                    });
                    selectionAreaItem.child(moreActionsSection);
                });
//                System.out.printf("%s %s %s", String.valueOf(selectionArea.getPos1().getX()), String.valueOf(selectionArea.getPos1().getY()), String.valueOf(selectionArea.getPos1().getZ()));
                component.child(selectionAreaItem);
            }
        });
    }
}
