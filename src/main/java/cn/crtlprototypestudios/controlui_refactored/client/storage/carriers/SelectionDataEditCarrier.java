package cn.crtlprototypestudios.controlui_refactored.client.storage.carriers;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class SelectionDataEditCarrier {
    private BlockPos block1, block2;
    public SelectionDataEditCarrier(BlockPos block1, BlockPos block2){
        this.block1 = block1;
        this.block2 = block2;
    }

    public BlockPos getBlock1() {
        return block1;
    }

    public BlockPos getBlock2() {
        return block2;
    }

    public void setBlock1(BlockPos block1) {
        this.block1 = block1;
    }

    public void setBlock2(BlockPos block2) {
        this.block2 = block2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SelectionDataEditCarrier that = (SelectionDataEditCarrier) obj;
        return Objects.equals(block1, that.block1) &&
                Objects.equals(block2, that.block2);
    }
}
