package dev.sterner;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class PeachBlockItem extends BlockItem {
    public PeachBlockItem(Block block, Settings settings) {
        super(block, settings.food(new FoodComponent.Builder().hunger(5).saturationModifier(4).meat().build()));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if(player != null && player.isSneaking()){
            return super.useOnBlock(context);
        }
        return ActionResult.PASS;
    }
}
