package dev.sterner.blockentity;

import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class ShimenawaBlockEntity extends BlockEntity {
    private BlockState logState = Blocks.OAK_LOG.getDefaultState();

    public ShimenawaBlockEntity(BlockPos pos, BlockState state) {
        super(NyctoPlusBlockEntityTypes.SHIMENAWA, pos, state);
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
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound logNbt = NbtHelper.fromBlockState(getLogState());
        nbt.put("Log", logNbt);
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
}
