package net.ironhorsedevgroup.mods.gunsmoke.materials;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.HashMap;
import java.util.Map;

public class MaterialUtils {
    private static final Map<ResourceLocation, Material> materials = new HashMap<>();

    public static void loadMaterials(String namespace, JsonArray paths, ResourceManager manager) {
        for (JsonElement entry : paths) {
            String path = entry.getAsString();
            materials.put(new ResourceLocation(namespace, path), Material.fromJson(Data.readJson(new ResourceLocation(namespace, path), manager)));
        }
    }

    public static Material getNull() {
        return new Material();
    }

    public static Material getMaterial(String namespace, String path) {
        return getMaterial(new ResourceLocation(namespace, path));
    }

    public static Material getMaterial(ResourceLocation location) {
        if (materials.containsKey(location)) {
            return materials.get(location);
        }
        return getNull();
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
            Properties properties = Properties.fromJson(json);
            Crafting crafting = Crafting.fromJson(json);

            return new Material(properties, crafting);
        }

        public static Material fromData(Data.DataObject data) {
            Properties properties = Properties.fromData(data);
            Crafting crafting = Crafting.fromData(data);

            return new Material(properties, crafting);
        }

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
