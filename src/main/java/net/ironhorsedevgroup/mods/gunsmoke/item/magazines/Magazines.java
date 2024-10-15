package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Guns;
import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Magazines {
    private static final Map<ResourceLocation, Magazine> magazines = new HashMap<>();

    public static void loadMagazines(List<ResourceLocation> magazines, MinecraftServer server) {
        clearMagazines();
        for (ResourceLocation magazine : magazines) {
            loadMagazine(magazine, server);
        }
    }

    public static void loadMagazine(ResourceLocation location, MinecraftServer server){
        Magazine magazine = Magazine.fromJson(DataLoader.loadJson(location, server));
        String[] strippedPath = location.getPath().split("/");
        location = new ResourceLocation(location.getNamespace(), strippedPath[strippedPath.length - 1]);
        Toolshed.LOGGER.info("Registering server magazine: {}", location);
        updateMagazine(location, magazine);
    }

    public static void updateMagazine(ResourceLocation location, Magazine magazine) {
        magazines.remove(location);
        magazines.put(location, magazine);
    }

    public static void clearMagazines() {
        magazines.clear();
    }

    public static Magazine getMagazine(ItemStack stack) {
        String magazine = NBT.getStringTag(stack, "magazine");
        String material = NBT.getStringTag(stack, "mag_material");
        if (!Objects.equals(magazine, "")) {
            if (!Objects.equals(material, "")) {
                return MaterialMagazine.fromItemStack(stack);
            }
            return getMagazine(magazine);
        }
        return GunMagazine.fromGun(Guns.getGun(stack));
    }

    public static Magazine getMagazine(String location) {
        return getMagazine(new ResourceLocation(location));
    }

    public static Magazine getMagazine(ResourceLocation location) {
        if (magazines.containsKey(location)) {
            return magazines.get(location);
        }
        return new DynamicMagazine();
    }
}
