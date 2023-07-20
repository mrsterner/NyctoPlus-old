package dev.sterner.mixin;

import dev.sterner.common.event.TickEquipmentEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow public abstract ItemStack getOffHandStack();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = {"tick"}, at = {@At("TAIL")})
    private void nyctoplus$tickEquipmentEvent(CallbackInfo ci) {
        Set<ItemStack> equipment = new HashSet<>();
        Iterable<ItemStack> itemStacks = this.getArmorItems();
        if (itemStacks != null) {
            itemStacks.forEach(equipment::add);
            equipment.add(this.getMainHandStack());
            equipment.add(this.getOffHandStack());
            TickEquipmentEvent.EVENT.invoker().tick(this.getWorld(), LivingEntity.class.cast(this), equipment);
        }
    }
}
