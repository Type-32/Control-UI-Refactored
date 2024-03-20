package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus.MenuScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPresetType;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPresetsData;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ControlUIRefactoredStorage;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.FileNameReferences;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ModalStorage;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;

import java.util.Map;

public class BlocksSelectionModalScreen extends MenuScreen {
    public BlocksSelectionModalScreen() {
        super("modals/blocks_selection_modal", "Blocks Selection", false);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        injectBlockListPreviewTemplate(rootComponent);
    }

    protected void injectBlockListPreviewTemplate(FlowLayout rootComponent) {
        FlowLayout blockPreviewHolder = rootComponent.childById(FlowLayout.class, "block-item-list-holder");
        assert blockPreviewHolder != null;
        blockPreviewHolder.clearChildren();
        blockPreviewHolder.<FlowLayout>configure(component -> {
            ModalStorage.getAllRegisteredBlocks();
            System.out.println("Cached Blocks: " + ModalStorage.cachedBlocksRegistry);
            for (Block block : ModalStorage.cachedBlocksRegistry) {
                FlowLayout blockItemPreview = component.child(this.model.expandTemplate(FlowLayout.class, "block-item-list-item@controlui_refactored:components/blocks_selection_list_item", Map.of(
                        "block-id", block.asItem().getDefaultStack().toString(),
                        "block-name", block.asItem().getDefaultStack().getName().toString()
                )));
                blockItemPreview.childById(ButtonComponent.class, "action.select-block-item").onPress(button -> {
                    ModalStorage.blocksSelection.add(block);
                    ScreenStackUtils.back();
                });
            }
        });
    }
}
