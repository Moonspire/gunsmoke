package net.ironhorsedevgroup.mods.gunsmoke.network;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.GunRenderPacket;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.PartRenderPacket;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundItemPacket;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundRenderPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class GunsmokeMessages {
    private static SimpleChannel INSTANCE;
    private static int packetID = 0;

    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Gunsmoke.MODID, "content_pack_messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PartRenderPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PartRenderPacket::decode)
                .encoder(PartRenderPacket::encode)
                .consumerMainThread(PartRenderPacket::handle)
                .add();

        net.messageBuilder(RoundRenderPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RoundRenderPacket::decode)
                .encoder(RoundRenderPacket::encode)
                .consumerMainThread(RoundRenderPacket::handle)
                .add();

        net.messageBuilder(RoundItemPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RoundItemPacket::decode)
                .encoder(RoundItemPacket::encode)
                .consumerMainThread(RoundItemPacket::handle)
                .add();

        net.messageBuilder(GunRenderPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GunRenderPacket::decode)
                .encoder(GunRenderPacket::encode)
                .consumerMainThread(GunRenderPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
