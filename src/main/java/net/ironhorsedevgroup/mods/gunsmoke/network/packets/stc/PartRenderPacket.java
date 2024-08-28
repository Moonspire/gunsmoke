package net.ironhorsedevgroup.mods.gunsmoke.network.packets.stc;

import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.parts.PartUtils;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PartRenderPacket {
    public ResourceLocation location;
    public ResourceLocation model;

    public PartRenderPacket(ResourceLocation location, PartUtils.Part part) {
        this.location = location;
        model = part.getRender().getModel();
    }

    public PartRenderPacket(ResourceLocation location, ResourceLocation model) {
        this.location = location;
        this.model = model;
    }

    public static PartRenderPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        ResourceLocation model = buf.readResourceLocation();
        return new PartRenderPacket(location, model);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeResourceLocation(model);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            PartUtils.loadPart(this);
        });
        return true;
    }
}
