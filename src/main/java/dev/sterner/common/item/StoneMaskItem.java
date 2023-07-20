package dev.sterner.common.item;

import dev.sterner.client.render.StoneMaskItemRenderer;
import dev.sterner.client.render.StoneMaskArmorRenderer;
import dev.sterner.common.event.TickEquipmentEvent;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.renderer.GeoArmorRenderer;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StoneMaskItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public static final String NBT = "StoneMaskNbt";
    public static final String IDLE = "Idle";
    public static final String AWAKENING = "Awakening";
    public static final String PIERCED = "Pierced";
    public static final String RETRACT = "Retract";

    private int timer = 0;

    public StoneMaskItem(Settings settings) {
        super(ArmorMaterials.LEATHER, Type.HELMET, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static void tick(World world, LivingEntity livingEntity, Set<ItemStack> equipment) {
        if (!world.isClient()) {
            Iterator<ItemStack> itemStackIterator = equipment.iterator();
            ItemStack head = itemStackIterator.next();
            if (head.getItem() instanceof StoneMaskItem stoneMaskItem) {
                stoneMaskItem.tickEquippedStoneMask(world, livingEntity, head);
            }
        }
    }

    private void tickEquippedStoneMask(World world, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.getNbt() == null) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString(NBT, IDLE);
            itemStack.writeNbt(nbt);
        } else {
            NbtCompound nbt = itemStack.getNbt();
            if (nbt.contains(NBT)) {
                String state = nbt.getString(NBT);
                switch (state) {
                    //TODO
                    case AWAKENING -> {}
                    case PIERCED -> {}
                    case RETRACT -> {}
                    default -> {}
                }
            }
        }
    }

    public boolean activateStoneMask(LivingEntity livingEntity, ItemStack itemStack){
        if (itemStack.getNbt() != null) {
            NbtCompound nbtCompound = itemStack.getNbt();
            if (nbtCompound.contains(NBT) && nbtCompound.getString(NBT).equals(IDLE)) {
                nbtCompound.putString(NBT, AWAKENING);
                return true;
            }
        }
        return false;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoArmorRenderer<?> renderer;
            private GeoItemRenderer<?> itemRenderer;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if(this.itemRenderer == null) {
                    this.itemRenderer = new StoneMaskItemRenderer();
                }

                return this.itemRenderer;
            }

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if(this.renderer == null) {
                    this.renderer = new StoneMaskArmorRenderer();
                }

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", this::idleState));
    }

    private PlayState idleState(AnimationState<StoneMaskItem> animationState) {
        AnimationController<StoneMaskItem> controller = animationState.getController();

        Entity entity = animationState.getData(DataTickets.ENTITY);

        if (entity instanceof ArmorStandEntity) {
            return PlayState.STOP;
        }

        Iterator<ItemStack> itemStackIterator = entity.getArmorItems().iterator();
        ItemStack head = itemStackIterator.next();
        if (head.isOf(this.asItem())) {
            if (head.getNbt() != null) {
                NbtCompound nbt =  head.getNbt();
                if (nbt.contains(NBT)) {
                    String state = nbt.getString(NBT);
                    RawAnimation animation = switch (state){
                        case AWAKENING -> RawAnimation.begin().thenLoop("awakening_full2");
                        case PIERCED -> RawAnimation.begin().thenLoop("pierced");
                        case RETRACT -> RawAnimation.begin().thenLoop("retract");
                        default -> RawAnimation.begin().thenLoop("inactive");
                    };
                    controller.setAnimation(animation);
                }
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
