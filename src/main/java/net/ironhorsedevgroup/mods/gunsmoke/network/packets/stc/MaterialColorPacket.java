package net.ironhorsedevgroup.mods.gunsmoke.network.packets.stc;

import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MaterialColorPacket {
    public ResourceLocation location;
    public int color;

    public MaterialColorPacket(ResourceLocation location, MaterialUtils.Material material) {
        this.location = location;
        color = material.getProperties().getColor();
    }

    public MaterialColorPacket(ResourceLocation location, int color) {
        this.location = location;
        this.color = color;
    }

    public static MaterialColorPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        int color = buf.readInt();
        return new MaterialColorPacket(location, color);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeInt(color);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            MaterialUtils.loadMaterial(this);
        });
        return true;
    }
}