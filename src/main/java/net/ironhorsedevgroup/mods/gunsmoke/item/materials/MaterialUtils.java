package net.ironhorsedevgroup.mods.gunsmoke.item.materials;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MaterialUtils {
    private static final Map<ResourceLocation, Material> materials = new HashMap<>();

    public static void loadMaterials(String namespace, JsonArray paths, ResourceManager manager) {
        for (JsonElement entry : paths) {
            String path = entry.getAsString();
            String[] strippedPath = path.split("/");
            ResourceLocation location = new ResourceLocation(namespace, strippedPath[strippedPath.length - 1]);
            ResourceLocation completeLocation = new ResourceLocation(namespace, path);
            Gunsmoke.LOGGER.info("Registering material from {}.json as {}", completeLocation, location);
            updateMaterial(location, Material.fromJson(Data.readJson(completeLocation, manager)));
        }
    }

    public static void loadMaterial(ResourceLocation location, Material material) {
        Gunsmoke.LOGGER.info("Registering material from packet as {}", location);
        updateMaterial(location, material);
    }

    public static List<ResourceLocation> getMaterials() {
        return materials.keySet().stream().toList();
    }

    public static void clearMaterials() {
        materials.clear();
    }

    public static void clearMaterial(ResourceLocation location) {
        materials.remove(location);
    }

    public static void updateMaterial(ResourceLocation location, Material material) {
        materials.remove(location);
        materials.put(location, material);
    }

    public static Material getNull() {
        return new Material();
    }

    public static Material getMaterial(String namespace, String path) {
        return getMaterial(new ResourceLocation(namespace, path));
    }

    public static Material getMaterial(String location) {
        if (!Objects.equals(location, null)) {
            return getMaterial(new ResourceLocation(location));
        }
        return getNull();
    }

    public static Material getMaterial(ResourceLocation location) {
        if (!Objects.equals(location, null)) {
            if (materials.containsKey(location)) {
               return materials.get(location);
            }
            Gunsmoke.LOGGER.warn("Could not locate material: {}", location);
        }
        return getNull();
    }

    public static String getMaterialLang(ResourceLocation location) {
        if (!Objects.equals(location, null) && materials.containsKey(location)) {
            return "gun_material." + location.getNamespace() + "." + location.getPath();
        }
        return "gun_material.null";
    }

    public static int getRoundColor(ItemStack itemStack, int tintIndex) {
        if (tintIndex < 2) {
            ResourceLocation location = new ResourceLocation(NBT.getStringTag(itemStack, "material_" + tintIndex));
            return getMaterial(location).getProperties().getColor();
        } else if (tintIndex == 3) {
            return 0;
        }
        return Color.getIntFromRGB(255, 255, 255);
    }

    public static class Material {
        private final Properties properties;
        private final Crafting crafting;

        public Material() {
            this.properties = new Properties();
            this.crafting = new Crafting();
        }

        private Material(Properties properties, Crafting crafting) {
            this.properties = properties;
            this.crafting = crafting;
        }

        public static Material fromJson(JsonObject json) {
            Data.DataObject data = new Data.DataObject(json);
            Properties properties = Properties.fromData(data.getObject("properties"));
            Crafting crafting = Crafting.fromData(data.getObject("crafting"));

            return new Material(properties, crafting);
        }

        public static Material fromData(Data.DataObject data) {
            Properties properties = Properties.fromData(data);
            Crafting crafting = Crafting.fromData(data);

            return new Material(properties, crafting);
        }

        /*
        public static Material fromPacket(MaterialPacket packet) {
            Properties properties = Properties.fromPacket(packet);
            Crafting crafting = new Crafting();

            return new Material(properties, crafting);
        }
        */

        public Properties getProperties() {
            return properties;
        }

        public Crafting getCrafting() {
            return crafting;
        }

        public static class Properties {
            private final int color;
            private final boolean flammable;
            private final int density;
            private final byte hardness;
            private final byte purity;

            private Properties() {
                this.color = 0;
                this.flammable = false;
                this.density = 1;
                this.hardness = 1;
                this.purity = 1;
            }

            private Properties(int color, boolean flammable, int density, byte hardness, byte purity) {
                this.color = color;
                this.flammable = flammable;
                this.density = density;
                this.hardness = hardness;
                this.purity = purity;
            }

            public static Properties fromJson(JsonObject json) {
                int color = 0;
                boolean flammable = false;
                int density = 0;
                byte hardness = 0;
                byte purity = 0;

                if (json.has("color")) {
                    JsonArray colorArray = json.getAsJsonArray("color");
                    color = Color.getIntFromRGB(colorArray.get(0).getAsInt(), colorArray.get(1).getAsInt(), colorArray.get(2).getAsInt());
                }
                if (json.has("flammable")) {
                    flammable = json.get("flammable").getAsBoolean();
                }
                if (json.has("density")) {
                    density = json.get("density").getAsInt();
                }
                if (json.has("hardness")) {
                    hardness = json.get("hardness").getAsByte();
                }
                if (json.has("purity")) {
                    purity = json.get("purity").getAsByte();
                }

                return new Properties(color, flammable, density, hardness, purity);
            }

            public static Properties fromData(Data.DataObject data) {
                return fromJson(data.get().getAsJsonObject());
            }

            /*
            public static Properties fromPacket(MaterialPacket packet) {
                return new Properties(
                        packet.color,
                        packet.flammable,
                        packet.density,
                        packet.hardness,
                        packet.purity
                        );
            }
            */

            public int getColor() {
                return color;
            }

            public boolean isFlammable() {
                return flammable;
            }

            public byte getHardness() {
                return hardness;
            }

            public byte getPurity() {
                return purity;
            }

            public int getDensity() {
                return density;
            }
        }

        public static class Crafting {
            private final ResourceLocation craftingLocation;
            private final String type;
            private final ResourceLocation castingFluid;
            private final boolean castable;

            public Crafting() {
                this.craftingLocation = null;
                this.type = null;
                this.castingFluid = null;
                this.castable = false;
            }

            private Crafting(ResourceLocation craftingLocation, String type, ResourceLocation castingFluid, boolean castable) {
                this.craftingLocation = craftingLocation;
                this.type = type;
                this.castingFluid = castingFluid;
                this.castable = castable;
            }

            public static Crafting fromJson(JsonObject json) {
                ResourceLocation craftingLocation = null;
                String type = null;
                ResourceLocation castingFluid = null;
                boolean castable = false;

                if (json.has("crafting_item")) {
                    JsonObject craftingItem = json.getAsJsonObject("crafting_item");

                    if (craftingItem.has("type")) {
                        type = craftingItem.get("type").getAsString();
                    }

                    craftingLocation = new ResourceLocation(craftingItem.get("ingredient").getAsString());
                }

                if (json.has("castable")) {
                    castable = json.get("castable").getAsBoolean();
                }

                if (castable || json.has("casting_fluid")) {
                    castingFluid = new ResourceLocation(json.get("casting_fluid").getAsString());
                }

                return new Crafting(craftingLocation, type, castingFluid, castable);
            }

            public static Crafting fromData(Data.DataObject data) {
                return fromJson(data.get().getAsJsonObject());
            }
        }
    }
}