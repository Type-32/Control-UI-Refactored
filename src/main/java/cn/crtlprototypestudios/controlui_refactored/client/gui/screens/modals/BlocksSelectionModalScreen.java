package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus.MenuScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ModalStorage;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.util.EventSource;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Map;

public class BlocksSelectionModalScreen extends MenuScreen {
    public BlocksSelectionModalScreen() {
        super("modals/blocks_selection_modal", "Blocks Selection", false);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        rootComponent.childById(ButtonComponent.class, "action.cancel-block-selection").onPress(component -> {
            ScreenStackUtils.back();
        });
    }

    @Override
    protected void init(){
        super.init();
        injectBlockListPreviewTemplate(ModalStorage.cachedBlocksRegistry);
        EventSource<TextBoxComponent.OnChanged> onSearchTextChanged = this.uiAdapter.rootComponent.childById(TextBoxComponent.class, "input.block-search-bar").onChanged();
        onSearchTextChanged.subscribe(component -> {
            // TODO: Optimize this function, potential stack overflow
            ArrayList<Block> temp = new ArrayList<>();
            for (Block block : ModalStorage.cachedBlocksRegistry.stream().filter(block -> block.asItem().getDefaultStack().getName().getString().toLowerCase().contains(component.toLowerCase())).toList())
                temp.add(block);

            injectBlockListPreviewTemplate(temp);
        });
    }

    protected void injectBlockListPreviewTemplate(ArrayList<Block> registry) {
        if (this.uiAdapter == null) return;

        FlowLayout blockPreviewHolder = this.uiAdapter.rootComponent.childById(FlowLayout.class, "block-item-list-holder");
        assert blockPreviewHolder != null;

        blockPreviewHolder.clearChildren();

        blockPreviewHolder.<FlowLayout>configure(component -> {
            ModalStorage.getAllRegisteredBlocks();
            for (Block block : registry) {
//                System.out.println(block.asItem().getDefaultStack().toString());

                FlowLayout blockItemPreview = component.child(this.model.expandTemplate(FlowLayout.class, "block-item-list-item@controlui_refactored:components/blocks_selection_list_item", Map.of(
                        "block-name", block.asItem().getDefaultStack().getName().getString()
                )));

                blockItemPreview.childById(ItemComponent.class, "icon.preview-block-item").stack(block.asItem().getDefaultStack());

                blockItemPreview.childById(ButtonComponent.class, "action.select-block-item").onPress(button -> {
                    ModalStorage.blocksSelection.add(block);
                    ScreenStackUtils.back();
                });
            }
        });
    }
}
