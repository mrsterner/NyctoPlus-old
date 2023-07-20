package dev.sterner.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Set;

//TODO remove since this is in base nycto
@FunctionalInterface
public interface TickEquipmentEvent {
    Event<TickEquipmentEvent> EVENT = EventFactory.createArrayBacked(TickEquipmentEvent.class, (listeners) -> {
        return (world, living, equipment) -> {
            TickEquipmentEvent[] var4 = listeners;
            int size = listeners.length;

            for(int i = 0; i < size; ++i) {
                TickEquipmentEvent event = var4[i];
                event.tick(world, living, equipment);
            }

        };
    });

    void tick(World world, LivingEntity livingEntity, Set<ItemStack> itemStacks);
}
