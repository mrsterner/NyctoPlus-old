package dev.sterner.common.item;

import dev.sterner.common.block.ShimenawaBlock;
import dev.sterner.common.blockentity.ShimenawaBlockEntity;
import dev.sterner.registry.NyctoPlusObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ShimenawaItem extends BlockItem {
    public ShimenawaItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState state = world.getBlockState(pos);
        if (state.isIn(BlockTags.LOGS)) {
            if (state.contains(Properties.AXIS)) {
                Direction.Axis axis = state.get(Properties.AXIS);
                if (axis == Direction.Axis.Y) {
                    world.setBlockState(pos, NyctoPlusObjects.SHIMENAWA.getDefaultState().with(ShimenawaBlock.ROTATION, ctx.getHorizontalPlayerFacing().getOpposite()));
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS);
                    if (world.getBlockEntity(pos) instanceof ShimenawaBlockEntity blockEntity) {
                        blockEntity.setLogState(state);
                    }
                    return ActionResult.CONSUME;
                }
            }
        }

        return super.useOnBlock(ctx);
    }
}
