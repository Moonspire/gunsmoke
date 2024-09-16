package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.network.GunsmokeMessages;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.GunRenderPacket;
import net.ironhorsedevgroup.mods.toolshed.content_packs.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Guns {
    private static final Map<ResourceLocation, DynamicGun> guns = new HashMap<>();
    private static final DynamicGun NULL = new DynamicGun();

    public static void loadGuns(List<ResourceLocation> guns, MinecraftServer server) {
        for (ResourceLocation gun : guns) {
            loadGun(gun, server);
        }
    }

    public static void loadGun(ResourceLocation location, MinecraftServer server) {
        DynamicGun gun = DynamicGun.fromJson(DataLoader.loadJson(location, server));
        String[] strippedPath = location.getPath().split("/");
        location = new ResourceLocation(location.getNamespace(), strippedPath[strippedPath.length - 1]);
        Gunsmoke.LOGGER.info("Registering server gun: {}", location);
        updateGun(location, gun);
    }

    public static void loadGun(GunRenderPacket packet) {
        ResourceLocation location = packet.location;
        Gunsmoke.LOGGER.info("Registering client gun: {}", location);
        updateGun(location, DynamicGun.fromPacket(packet));
    }

    public static void updateGun(ResourceLocation location, DynamicGun gun) {
        guns.remove(location);
        guns.put(location, gun);
    }

    public static void sendGuns(ServerPlayer player) {
        for (ResourceLocation gun : guns.keySet()) {
            GunsmokeMessages.sendToPlayer(new GunRenderPacket(gun, guns.get(gun)), player);
        }
    }

    public static Gun getGun(ItemStack stack) {
        return getGun(NBT.getLocationTag(stack, "gun"));
    }

    public static DynamicGun getGun(ResourceLocation location) {
        if (guns.containsKey(location)) {
            return guns.get(location);
        }
        return NULL;
    }

    public static DynamicGun getGun(String location) {
        return getGun(new ResourceLocation(location));
    }

    public static DynamicGun getNull() {
        return NULL;
    }

    public static Map<ResourceLocation, DynamicGun> getAllGuns() {
        return guns;
    }

    public static boolean hasGun(String location) {
        return hasGun(new ResourceLocation(location));
    }

    public static boolean hasGun(ResourceLocation location) {
        return guns.containsKey(location);
    }

    public static ResourceLocation getModel(ResourceLocation gun) {
        if (guns.containsKey(gun)) {
            return guns.get(gun).getRender().getModel();
        }
        return NULL.getRender().getModel();
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        ResourceLocation material;
        switch (tintIndex) {
            case 0:
                material = NBT.getLocationTag(stack, "barrel");
                break;
            case 1:
                material = NBT.getLocationTag(stack, "breach");
                break;
            case 2:
                material = NBT.getLocationTag(stack, "core");
                break;
            case 3:
                material = NBT.getLocationTag(stack, "stock");
                break;
            default:
                return Color.getIntFromRGB(255, 255, 255);
        }
        return Materials.getMaterial(material).getProperties().getColor();
    }
}
