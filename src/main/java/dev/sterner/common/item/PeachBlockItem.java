package dev.sterner.common.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;

public class PeachBlockItem extends BlockItem {
    public PeachBlockItem(Block block, Settings settings) {
        super(block, settings.food(new FoodComponent.Builder().hunger(5).saturationModifier(4).meat().build()));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking()) {
            return super.useOnBlock(context);
        }
        return ActionResult.PASS;
    }

    @Override
    public void postProcessNbt(NbtCompound nbt) {
        super.postProcessNbt(nbt);
        if (nbt.contains("SkullOwner", NbtElement.STRING_TYPE) && !Util.isBlank(nbt.getString("SkullOwner"))) {
            GameProfile gameProfile = new GameProfile(null, nbt.getString("SkullOwner"));
            SkullBlockEntity.loadProperties(gameProfile, (profile) -> {
                nbt.put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile));

                NbtCompound skull = nbt.getCompound("Type");
                NbtCompound typeCompound = new NbtCompound();
                typeCompound.putString("Skull", skull.getString("Skull"));
                nbt.put("Type", typeCompound);
            });
        }

    }
}
