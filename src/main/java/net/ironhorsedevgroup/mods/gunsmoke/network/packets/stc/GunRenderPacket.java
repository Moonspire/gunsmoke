package net.ironhorsedevgroup.mods.gunsmoke.network.packets.stc;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GunRenderPacket {
    public ResourceLocation location;
    public ResourceLocation model;

    public GunRenderPacket(ResourceLocation location, GunUtils.Gun gun) {
        this.location = location;
        model = gun.getRender().getModel();
    }

    public GunRenderPacket(ResourceLocation location, ResourceLocation model) {
        this.location = location;
        this.model = model;
    }

    public static GunRenderPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        ResourceLocation model = buf.readResourceLocation();
        return new GunRenderPacket(location, model);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeResourceLocation(model);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            GunUtils.loadGun(this);
        });
        return true;
    }
}
