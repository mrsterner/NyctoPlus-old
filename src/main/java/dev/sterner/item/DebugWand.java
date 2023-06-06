package dev.sterner.item;

import dev.sterner.block.LivingCoreLogBlock;
import dev.sterner.block.PeachLogBlock;
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
            LivingCoreLogBlock.generateTree(world, blockPos);

            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
}
