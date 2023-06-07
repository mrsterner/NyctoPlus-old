package dev.sterner.blockentity;

import com.mojang.authlib.GameProfile;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.entity.SkullBlockEntity.loadProperties;

public class PeachBlockEntity extends BlockEntity {
    @Nullable
    private GameProfile owner;
    private Type type;

    public PeachBlockEntity(BlockPos pos, BlockState state) {
        super(NyctoPlusBlockEntityTypes.PEACH_BLOCK_ENTITY, pos, state);
    }

    public Type getSkullType() {
        return this.type;
    }

    public void setSkullType(Type type) {
        this.type = type;
        this.markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.owner != null) {
            NbtCompound nbtCompound = new NbtCompound();
            NbtHelper.writeGameProfile(nbtCompound, this.owner);
            nbt.put("SkullOwner", nbtCompound);
        }
        if (getSkullType() != null) {
            NbtCompound typeCompound = new NbtCompound();
            typeCompound.putString("Skull", getSkullType().name());
            nbt.put("Type", typeCompound);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
            this.setOwner(NbtHelper.toGameProfile(nbt.getCompound("SkullOwner")));
        }
        if (nbt.contains("Type", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = nbt.getCompound("Type");
            this.setSkullType(Type.valueOf(nbtCompound.getString("Skull")));
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

    public enum Type implements StringIdentifiable {
        PLAYER("piglin"),
        VILLAGER("villager"),
        PILLAGER("pillager"),
        PIGLIN("piglin");

        final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this == PLAYER ? PLAYER.name : this == VILLAGER ? VILLAGER.name : this == PILLAGER ? PILLAGER.name : PIGLIN.name;
        }
    }
}
