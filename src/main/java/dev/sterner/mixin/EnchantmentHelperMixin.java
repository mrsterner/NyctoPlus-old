package dev.sterner.mixin;

import dev.sterner.common.item.StoneMaskItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "hasBindingCurse", at = @At("RETURN"), cancellable = true)
    private static void nyctoplus$bindStoneMask(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (stack.getItem() instanceof StoneMaskItem && stack.getNbt() != null) {
            NbtCompound nbt = stack.getNbt();
            if (nbt.contains(StoneMaskItem.NBT) && !nbt.getString(StoneMaskItem.NBT).equals(StoneMaskItem.IDLE)) {
                cir.setReturnValue(true);
            }
        }
    }
}
