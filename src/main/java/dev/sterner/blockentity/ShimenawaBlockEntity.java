package dev.sterner.blockentity;

import dev.sterner.block.PeachGrowthManager;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.registry.NyctoPlusTagKeys;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

public class ShimenawaBlockEntity extends BlockEntity implements GameEventListener.Holder<ShimenawaBlockEntity.Listener> {
    private BlockState logState = Blocks.OAK_LOG.getDefaultState();
    private final Listener eventListener;
    private int deathCount = 0;

    public ShimenawaBlockEntity(BlockPos pos, BlockState state) {
        super(NyctoPlusBlockEntityTypes.SHIMENAWA, pos, state);
        this.eventListener = new Listener(new BlockPositionSource(pos), this);
    }

    public void onUse(World world, BlockPos pos, PlayerEntity player) {
    }

    public BlockState getLogState() {
        return logState;
    }

    public void setLogState(BlockState state){
        this.logState = state;
        markDirty();
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (this.world != null) {
            NbtCompound logNbt = nbt.getCompound("Log");
            setLogState(NbtHelper.toBlockState(this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK), logNbt));
        }
        setDeathCount(nbt.getInt("DeathCount"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound logNbt = NbtHelper.fromBlockState(getLogState());
        nbt.put("Log", logNbt);
        nbt.putInt("DeathCount", getDeathCount());
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isClient) {
            world.updateListeners(pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
            this.toUpdatePacket();
        }
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(pos);
        }
    }

    public int getDeathCount(){
        return deathCount;
    }

    public void addToDeathCount(LivingEntity livingEntity) {
        if (livingEntity.getType().isIn(NyctoPlusTagKeys.HUMANOIDS)) {
            setDeathCount(getDeathCount() + 5);
        } else {
            setDeathCount(getDeathCount() + 1);
        }
    }

    public void setDeathCount(int deathCount){
        this.deathCount = deathCount;
        markDirty();
    }

    @Override
    public Listener getEventListener() {
        return eventListener;
    }

    public static class Listener implements GameEventListener {
        private final PositionSource positionSource;
        private final ShimenawaBlockEntity blockEntity;

        public Listener(PositionSource positionSource, ShimenawaBlockEntity blockEntity) {
            this.positionSource = positionSource;
            this.blockEntity = blockEntity;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public int getRange() {
            return 12;
        }

        @Override
        public GameEventListener.TriggerOrder getTriggerOrder() {
            return GameEventListener.TriggerOrder.BY_DISTANCE;
        }

        @Override
        public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (event == GameEvent.ENTITY_DIE) {
                Entity entity = emitter.sourceEntity();
                if (entity instanceof LivingEntity livingEntity) {
                    this.blockEntity.addToDeathCount(livingEntity);
                    return true;
                }
            }
            return false;
        }
    }


}
