package dev.sterner.blockentity;

import dev.sterner.block.PeachGrowthManager;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeachCoreBlockEntity extends BlockEntity implements GameEventListener.Holder<PeachCoreBlockEntity.Listener> {
    private final Listener eventListener;
    private final List<BlockPos> availableGrowthNodes = new ArrayList<>();

    public PeachCoreBlockEntity(BlockPos pos, BlockState state) {
        super(NyctoPlusBlockEntityTypes.PEACH_CORE_BLOCK_ENTITY, pos, state);
        this.eventListener = new Listener(new BlockPositionSource(pos), this);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        eventListener.manager.tick(world, pos, world.getRandom());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.eventListener.manager.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        this.eventListener.manager.writeNbt(nbt);
        super.writeNbt(nbt);
    }

    private BlockPos getGrowPos() {
        if (availableGrowthNodes.isEmpty() || world == null) {
            return null;
        }
        int picked = world.getRandom().nextBetween(0, availableGrowthNodes.size());
        BlockPos pos = availableGrowthNodes.get(picked);
        availableGrowthNodes.remove(picked);
        return pos;
    }

    @Override
    public Listener getEventListener() {
        return this.eventListener;
    }

    public static class Listener implements GameEventListener {
        private final PositionSource positionSource;
        private final PeachGrowthManager manager;
        private final PeachCoreBlockEntity blockEntity;

        public Listener(PositionSource positionSource, PeachCoreBlockEntity peachCoreBlockEntity) {
            this.positionSource = positionSource;
            this.manager = PeachGrowthManager.create();
            this.blockEntity = peachCoreBlockEntity;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public int getRange() {
            return 8;
        }

        @Override
        public GameEventListener.TriggerOrder getTriggerOrder() {
            return GameEventListener.TriggerOrder.BY_DISTANCE;
        }

        @Override
        public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (event == GameEvent.ENTITY_DIE) {
                Entity entity = emitter.sourceEntity();
                if (entity instanceof PlayerEntity playerEntity && blockEntity.getGrowPos() != null) {
                    this.manager.growHead(playerEntity, blockEntity.getGrowPos());
                    return true;
                }
            }
            return false;
        }
    }
}
