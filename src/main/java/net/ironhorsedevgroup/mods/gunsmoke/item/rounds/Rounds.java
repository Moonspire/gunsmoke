package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.network.GunsmokeMessages;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundItemPacket;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundRenderPacket;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rounds {
    private static final Map<String, Map<String, Round>> rounds = new HashMap<>();
    private static final Map<ResourceLocation, List<String>> roundItems = new HashMap<>();

    public static void loadRounds(List<ResourceLocation> rounds, MinecraftServer server) {
        for (ResourceLocation round : rounds) {
            loadRound(round, server);
        }
    }

    public static void loadRound(ResourceLocation location, MinecraftServer server) {
        String[] strippedPath = location.getPath().split("/");
        Round round = Round.fromJson(DataLoader.loadJson(location, server), strippedPath[strippedPath.length - 1]);
        location = round.getLocation();
        Gunsmoke.LOGGER.info("Registering server round: {}", location);
        updateRound(location, round);
    }

    public static void loadRound(RoundItemPacket packet) {
        ResourceLocation location = packet.location;
        Gunsmoke.LOGGER.info("Registering client item round: {}", location);
        updateRound(location, ItemRound.fromPacket(packet));
    }

    public static void loadRound(RoundRenderPacket packet) {
        ResourceLocation location = packet.location;
        Gunsmoke.LOGGER.info("Registering client dynamic round: {}", location);
        updateRound(location, DynamicRound.fromPacket(packet));
    }

    public static void updateRound(ResourceLocation location, Round round) {
        if (!rounds.containsKey(location.getNamespace())) {
            rounds.put(location.getNamespace(), new HashMap<>());
        }
        rounds.get(location.getNamespace()).remove(location.getPath());
        rounds.get(location.getNamespace()).put(location.getPath(), round);
    }

    public static Map<String, Map<String, Round>> getAllRounds() {
        return rounds;
    }

    public static Map<String, Round> getCaliber(String caliber) {
        if (rounds.containsKey(caliber)) {
            return rounds.get(caliber);
        }
        return new HashMap<>();
    }

    public static Round getRound(ResourceLocation location) {
        Map<String,Round> caliber = getCaliber(location.getNamespace());
        if (caliber.containsKey(location.getPath())) {
            return caliber.get(location.getPath());
        }
        return null;
    }

    public static Round getRound(ItemStack itemStack) {
        if (itemStack.getItem() instanceof RoundItem) {
            return getRound(NBT.getLocationTag(itemStack, "round_id"));
        }
        return null;
    }

    public static void clearRounds() {
        rounds.clear();
        roundItems.clear();
    }

    public static void sendRounds(ServerPlayer player) {
        for (String caliberId : rounds.keySet()) {
            for (String roundId : rounds.get(caliberId).keySet()) {
                ResourceLocation location = new ResourceLocation(caliberId, roundId);
                Round round = getRound(location);
                if (round instanceof ItemRound itemRound) {
                    GunsmokeMessages.sendToPlayer(new RoundItemPacket(location, itemRound), player);
                } else if (round instanceof DynamicRound dynamRound) {
                    GunsmokeMessages.sendToPlayer(new RoundRenderPacket(location, dynamRound), player);
                }
            }
        }
    }

    public static boolean isItemRound(ResourceLocation location) {
        return roundItems.containsKey(location);
    }

    public static void setItemRound(ResourceLocation location, String caliber) {
        if (!roundItems.containsKey(location)) {
            roundItems.put(location, new ArrayList<>());
        }
        if (!roundItems.get(location).contains(caliber)) {
            roundItems.get(location).add(caliber);
        }
    }

    public static List<String> getCalibers(Item item) {
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(item);
        if (isItemRound(location)) {
            return roundItems.get(location);
        }
        return new ArrayList<>();
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        ResourceLocation material;
        switch (tintIndex) {
            case 0:
                material = NBT.getLocationTag(stack, "round");
                break;
            case 1:
                material = NBT.getLocationTag(stack, "casing");
                break;
            case 2:
                return getRound(stack).getRender().getColor();
            default:
                return Color.getIntFromRGB(255, 255, 255);
        }
        return Materials.getMaterial(material).getProperties().getColor();
    }
}
