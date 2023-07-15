package dev.sterner.common.blockentity;

import dev.sterner.common.block.LivingCoreLogBlock;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.registry.NyctoPlusObjects;
import dev.sterner.registry.NyctoPlusTagKeys;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
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
    private BlockState logState;
    private final Listener eventListener;
    private int deathCount = 0;
    private boolean bl = true;

    public ShimenawaBlockEntity(BlockPos pos, BlockState state) {
        super(NyctoPlusBlockEntityTypes.SHIMENAWA, pos, state);
        this.eventListener = new Listener(new BlockPositionSource(pos), this);
        this.logState = Blocks.OAK_LOG.getDefaultState();
    }

    public void onUse(World world, BlockPos pos, PlayerEntity player) {
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient()) {
            if (bl && deathCount >= 10 && logState.isOf(Blocks.OAK_LOG) && world.getBlockState(pos.down()).isOf(Blocks.OAK_LOG)) {
                bl = false;
                BlockPos startGrowPos = getBottomLog(world, pos);
                LivingCoreLogBlock.generateTree(world, startGrowPos);
                markDirty();
            }
        }
    }

    private BlockPos getBottomLog(World world, BlockPos startPos) {
        if (world.getBlockState(startPos).isOf(Blocks.OAK_LOG) || world.getBlockState(startPos).isOf(NyctoPlusObjects.SHIMENAWA)) {
            int height = startPos.getY() - 1;
            while (height > world.getBottomY()) {
                BlockPos logPos = new BlockPos(startPos.getX(), height, startPos.getZ());
                if (world.getBlockState(logPos).isOf(Blocks.OAK_LOG)) {
                    height--;
                } else {
                    return logPos.up();
                }
            }
            return startPos.down();
        }
        return startPos;
    }

    public BlockState getLogState() {
        return logState;
    }

    public void setLogState(BlockState state) {
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
        RegistryEntryLookup<Block> registryEntryLookup = this.world != null ? this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK) : Registries.BLOCK.getReadOnlyWrapper();
        setLogState(NbtHelper.toBlockState(registryEntryLookup, nbt.getCompound("Log")));
        setDeathCount(nbt.getInt("DeathCount"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("Log", NbtHelper.fromBlockState(getLogState()));
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

    public int getDeathCount() {
        return deathCount;
    }

    public void addToDeathCount(LivingEntity livingEntity) {
        if (livingEntity.getType().isIn(NyctoPlusTagKeys.HUMANOIDS)) {
            setDeathCount(getDeathCount() + 5);
        } else {
            if (livingEntity instanceof HostileEntity) {
                return;
            }
            setDeathCount(getDeathCount() + 1);
        }
    }

    public void setDeathCount(int deathCount) {
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
