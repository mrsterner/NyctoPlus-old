package dev.sterner.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class PeachLogBlock extends PillarBlock {
    public static final IntProperty VARIANTS = IntProperty.of("variants", 0, 1);

    public PeachLogBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(VARIANTS, 0).with(AXIS, Direction.Axis.Y));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(VARIANTS, ctx.getWorld().random.nextInt(2));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VARIANTS, AXIS);
    }
}
