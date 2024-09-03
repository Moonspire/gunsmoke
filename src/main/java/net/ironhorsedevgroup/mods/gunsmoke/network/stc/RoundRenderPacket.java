package net.ironhorsedevgroup.mods.gunsmoke.network.stc;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RoundRenderPacket {
    public ResourceLocation location;
    public ResourceLocation model;
    public int color;

    public RoundRenderPacket(ResourceLocation location, RoundUtils.DynamicRound round) {
        this.location = location;
        color = round.getRender().getColor();
        model = round.getRender().getModel();
    }

    public RoundRenderPacket(ResourceLocation location, ResourceLocation model, int color) {
        this.location = location;
        this.model = model;
        this.color = color;
    }

    public static RoundRenderPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        ResourceLocation model = buf.readResourceLocation();
        int color = buf.readInt();
        return new RoundRenderPacket(location, model, color);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeResourceLocation(model);
        buf.writeInt(color);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            RoundUtils.loadRound(this);
        });
        return true;
    }
}
