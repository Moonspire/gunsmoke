package net.ironhorsedevgroup.mods.gunsmoke.network.stc;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.DynamicGun;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Guns;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GunRenderPacket {
    public ResourceLocation location;
    public ResourceLocation model;
    public DynamicGun.Composition composition;

    public GunRenderPacket(ResourceLocation location, DynamicGun gun) {
        this.location = location;
        model = gun.getRender().getModel();
        composition = gun.getComposition();
    }

    public GunRenderPacket(ResourceLocation location, ResourceLocation model, DynamicGun.Composition composition) {
        this.location = location;
        this.model = model;
        this.composition = composition;
    }

    public static GunRenderPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        ResourceLocation model = buf.readResourceLocation();
        DynamicGun.Composition composition = DynamicGun.Composition.fromPacket(buf);
        return new GunRenderPacket(location, model, composition);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeResourceLocation(model);
        buf.writeResourceLocation(composition.getBarrel().getMaterial());
        buf.writeResourceLocation(composition.getBreach().getMaterial());
        buf.writeResourceLocation(composition.getCore().getMaterial());
        buf.writeResourceLocation(composition.getStock().getMaterial());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Guns.loadGun(this);
        });
        return true;
    }
}
