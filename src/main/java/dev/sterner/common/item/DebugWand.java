package dev.sterner.common.item;

import dev.sterner.common.block.LivingCoreLogBlock;
import dev.sterner.common.blockentity.LivingCoreBlockEntity;
import dev.sterner.common.blockentity.PeachBlockEntity;
import dev.sterner.registry.NyctoPlusObjects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugWand extends Item {
    public DebugWand(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        if (!world.isClient()) {
            if (world.getBlockState(blockPos).isOf(NyctoPlusObjects.PEACH)) {
                if (world.getBlockEntity(blockPos) instanceof PeachBlockEntity blockEntity) {
                    PeachBlockEntity.Type type = blockEntity.getSkullType();
                    if (type == null) {
                        blockEntity.setSkullType(PeachBlockEntity.Type.PLAYER);
                        blockEntity.markDirty();
                    }
                    if (type != null) {
                        switch (type) {
                            case PIGLIN -> blockEntity.setSkullType(PeachBlockEntity.Type.VILLAGER);
                            case PLAYER -> blockEntity.setSkullType(PeachBlockEntity.Type.PILLAGER);
                            case PILLAGER -> blockEntity.setSkullType(PeachBlockEntity.Type.PIGLIN);
                            default -> blockEntity.setSkullType(PeachBlockEntity.Type.PLAYER);
                        }
                    }
                }
                return ActionResult.CONSUME;
            } else if (world.getBlockState(blockPos).isOf(NyctoPlusObjects.LIVING_CORE_LOG)) {
                if (world.getBlockEntity(blockPos) instanceof LivingCoreBlockEntity blockEntity) {
                    System.out.println("Leaves: " + blockEntity.leaves);
                    System.out.println("Nodes: " + blockEntity.availableGrowthNodes);
                    return ActionResult.CONSUME;
                }
            } else {
                LivingCoreLogBlock.generateTree(world, blockPos);
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
}
