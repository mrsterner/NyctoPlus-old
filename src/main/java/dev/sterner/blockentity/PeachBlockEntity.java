package dev.sterner.blockentity;

import com.mojang.authlib.GameProfile;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.entity.SkullBlockEntity.loadProperties;

public class PeachBlockEntity extends BlockEntity {
    @Nullable
    private static UserCache userCache;
    @Nullable
    private GameProfile owner;

    public PeachBlockEntity(BlockPos pos, BlockState state) {
        super(NyctoPlusBlockEntityTypes.PEACH_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.owner != null) {
            NbtCompound nbtCompound = new NbtCompound();
            NbtHelper.writeGameProfile(nbtCompound, this.owner);
            nbt.put("SkullOwner", nbtCompound);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
            this.setOwner(NbtHelper.toGameProfile(nbt.getCompound("SkullOwner")));
        }
    }

    public void setOwner(@Nullable GameProfile owner) {
        synchronized (this) {
            this.owner = owner;
        }

        this.loadOwnerProperties();
    }

    private void loadOwnerProperties() {
        loadProperties(this.owner, (owner) -> {
            this.owner = owner;
            this.markDirty();
        });
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Nullable
    public GameProfile getOwner() {
        return this.owner;
    }
}
