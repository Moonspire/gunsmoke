package net.ironhorsedevgroup.mods.gunsmoke.network.stc;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.ItemRound;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Rounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RoundItemPacket {
    public ResourceLocation location;
    public ResourceLocation item;

    public RoundItemPacket(ResourceLocation location, ItemRound round) {
        this.location = location;
        item = round.getItemLocation();
    }

    public RoundItemPacket(ResourceLocation location, ResourceLocation item) {
        this.location = location;
        this.item = item;
    }

    public static RoundItemPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        ResourceLocation item = buf.readResourceLocation();
        return new RoundItemPacket(location, item);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeResourceLocation(item);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Rounds.loadRound(this);
        });
        return true;
    }
}