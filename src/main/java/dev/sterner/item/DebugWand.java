package dev.sterner.item;

import dev.sterner.NyctoPlus;
import dev.sterner.block.LivingCoreLogBlock;
import dev.sterner.block.PeachBlock;
import dev.sterner.block.PeachLogBlock;
import dev.sterner.blockentity.PeachBlockEntity;
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
        if(!world.isClient()){
            if(world.getBlockState(blockPos).isOf(NyctoPlusObjects.PEACH)){
                if(world.getBlockEntity(blockPos) instanceof PeachBlockEntity blockEntity){
                    PeachBlock.Type type = blockEntity.getSkullType();
                    if(type == null){
                        blockEntity.setSkullType(PeachBlock.Type.PLAYER);
                        blockEntity.markDirty();
                    }
                    if (type != null) {
                        switch (type) {
                            case PIGLIN -> blockEntity.setSkullType(PeachBlock.Type.VILLAGER);
                            case VILLAGER -> blockEntity.setSkullType(PeachBlock.Type.PLAYER);
                            case PLAYER -> blockEntity.setSkullType(PeachBlock.Type.PILLAGER);
                            case PILLAGER -> blockEntity.setSkullType(PeachBlock.Type.PIGLIN);
                            default -> blockEntity.setSkullType(PeachBlock.Type.PLAYER);
                        }
                    }
                }
                return ActionResult.CONSUME;
            } else {
                LivingCoreLogBlock.generateTree(world, blockPos);
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
}
