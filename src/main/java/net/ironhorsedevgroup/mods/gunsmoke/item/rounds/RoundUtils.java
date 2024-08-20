package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundUtils {
    private static final Map<String, Map<String, Round>> rounds = new HashMap<>();
    private static final Map<ResourceLocation, List<String>> roundItems = new HashMap<>();

    public static void loadRounds(String namespace, JsonArray paths, ResourceManager manager) {
        for (JsonElement entry : paths) {
            String path = entry.getAsString();
            String[] strippedPath = path.split("/");
            ResourceLocation completeLocation = new ResourceLocation(namespace, path);
            Round round = Round.fromJson(Data.readJson(completeLocation, manager));
            ResourceLocation location = new ResourceLocation(round.getCaliber(), namespace + "." + strippedPath[strippedPath.length - 1]);
            Gunsmoke.LOGGER.info("Registering round from {}.json as {}", completeLocation, location);
            updateRound(location, round);
        }
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

    public static void clearRounds() {
        rounds.clear();
        roundItems.clear();
    }

    public static boolean isItemRound(ResourceLocation location) {
        return roundItems.containsKey(location);
    }

    public static List<String> getCalibers(Item item) {
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(item);
        if (isItemRound(location)) {
            return roundItems.get(location);
        }
        return new ArrayList<>();
    }

    public interface Round {
        static Round fromJson(JsonObject json) {
            if (json.has("item")) {
                return ItemRound.fromJson(json);
            }
            return DynamicRound.fromJson(json);
        };

        String getCaliber();

        Damage getDamage();

        Properties getProperties();

        boolean hasRenderer();

        class Damage {
            private final float entityDamage;
            private final int barrelDamage;
            private final int breachDamage;
            private final int coreDamage;
            private final int stockDamage;

            public Damage () {
                this.entityDamage = 1.0f;
                this.barrelDamage = 1;
                this.breachDamage = 1;
                this.coreDamage = 1;
                this.stockDamage = 1;
            }

            private Damage(float entityDamage, int barrelDamage, int breachDamage, int coreDamage, int stockDamage) {
                this.entityDamage = entityDamage;
                this.barrelDamage = barrelDamage;
                this.breachDamage = breachDamage;
                this.coreDamage = coreDamage;
                this.stockDamage = stockDamage;
            }

            public static Damage fromJson(JsonObject json) {
                float entityDamage = 1.0f;
                int barrelDamage = 1;
                int breachDamage = 1;
                int coreDamage = 1;
                int stockDamage = 1;

                if (json.has("entity_damage")) {
                    entityDamage = json.get("entity_damage").getAsFloat();
                }
                if (json.has("barrel_damage")) {
                    barrelDamage = json.get("barrel_damage").getAsInt();
                }
                if (json.has("breach_damage")) {
                    breachDamage = json.get("breach_damage").getAsInt();
                }
                if (json.has("core_damage")) {
                    coreDamage = json.get("core_damage").getAsInt();
                }
                if (json.has("stock_damage")) {
                    stockDamage = json.get("stock_damage").getAsInt();
                }

                return new Damage(entityDamage, barrelDamage, breachDamage, coreDamage, stockDamage);
            }

            public float getEntityDamage() {
                return entityDamage;
            }

            public int getBarrelDamage() {
                return barrelDamage;
            }

            public int getBreachDamage() {
                return breachDamage;
            }

            public int getCoreDamage() {
                return coreDamage;
            }

            public int getStockDamage() {
                return stockDamage;
            }
        }

        class Properties {
            private final boolean damageDropoff;
            private final boolean hasGravity;
            private final int life;
            private final int projectiles;
            private final float size;
            private final double speed;
            private final int trailColor;
            private final double trailLength;
            private final boolean visible;

            public Properties() {
                this.damageDropoff = true;
                this.hasGravity = true;
                this.life = 10;
                this.projectiles = 1;
                this.size = 0.2f;
                this.speed = 10.0;
                this.trailColor = Color.getIntFromRGB(255, 210, 137);
                this.trailLength = 1.0;
                this.visible = true;
            }

            private Properties(boolean damageDropoff, boolean hasGravity, int life, int projectiles, float size, double speed, int trailColor, double trailLength, boolean visible) {
                this.damageDropoff = damageDropoff;
                this.hasGravity = hasGravity;
                this.life = life;
                this.projectiles = projectiles;
                this.size = size;
                this.speed = speed;
                this.trailColor = trailColor;
                this.trailLength = trailLength;
                this.visible = visible;
            }

            public static Properties fromJson(JsonObject json) {
                boolean damageDropoff = true;
                boolean hasGravity = true;
                int life = 10;
                int projectiles = 1;
                float size = 0.2f;
                double speed = 10.0;
                int trailColor = Color.getIntFromRGB(255, 210, 137);
                double trailLength = 1.0;
                boolean visible = true;

                if (json.has("damage_drop")) {
                    damageDropoff = json.get("damage_drop").getAsBoolean();
                }
                if (json.has("has_gravity")) {
                    hasGravity = json.get("has_gravity").getAsBoolean();
                }
                if (json.has("life")) {
                    life = json.get("life").getAsInt();
                }
                if (json.has("projectiles")) {
                    projectiles = json.get("projectiles").getAsInt();
                }
                if (json.has("size")) {
                    size = json.get("size").getAsFloat();
                }
                if (json.has("speed")) {
                    speed = json.get("speed").getAsDouble();
                }
                if (json.has("trail_color")) {
                    JsonArray colorArray = json.getAsJsonArray("trail_color");
                    trailColor = Color.getIntFromRGB(colorArray.get(0).getAsInt(), colorArray.get(1).getAsInt(), colorArray.get(2).getAsInt());
                }
                if (json.has("trail_length")) {
                    trailLength = json.get("trail_length").getAsDouble();
                }
                if (json.has("visible")) {
                    visible = json.get("visible").getAsBoolean();
                }

                return new Properties(damageDropoff, hasGravity, life, projectiles, size, speed, trailColor, trailLength, visible);
            }

            public boolean hasDamageDropoff() {
                return damageDropoff;
            }

            public boolean hasGravity() {
                return hasGravity;
            }

            public int getLife() {
                return life;
            }

            public int getProjectiles() {
                return projectiles;
            }

            public float getSize() {
                return size;
            }

            public double getSpeed() {
                return speed;
            }

            public int getTrailColor() {
                return trailColor;
            }

            public double getTrailLength() {
                return trailLength;
            }

            public boolean isVisible() {
                return visible;
            }
        }
    }

    public static class DynamicRound implements Round {
        private final String caliber;
        private final Damage damage;
        private final Properties properties;
        private final Render render;

        private DynamicRound(String caliber, Damage damage, Properties properties, Render render) {
            this.caliber = caliber;
            this.damage = damage;
            this.properties = properties;
            this.render = render;
        }

        public static Round fromJson(JsonObject json) {
            String caliber = json.get("caliber").getAsString();
            Damage damage = new Damage();
            Properties properties = new Properties();
            Render render = new Render();

            if (json.has("damage")) {
                damage = Damage.fromJson(json.getAsJsonObject("damage"));
            }
            if (json.has("properties")) {
                properties = Properties.fromJson(json.getAsJsonObject("properties"));
            }
            if (json.has("render")) {
                render = Render.fromJson(json.getAsJsonObject("render"));
            }

            return new DynamicRound(caliber, damage, properties, render);
        }

        @Override
        public String getCaliber() {
            return caliber;
        }

        @Override
        public Damage getDamage() {
            return damage;
        }

        @Override
        public Properties getProperties() {
            return properties;
        }

        @Override
        public boolean hasRenderer() {
            return true;
        }

        public Render getRender() {
            return render;
        }

        public static class Render {
            private final int roundColor;
            private final ResourceLocation round;
            private final ResourceLocation casing;
            private final ResourceLocation accessories;
            private final ResourceLocation color;

            public Render() {
                this.roundColor = Color.getIntFromRGB(255, 255, 255);
                this.round = new ResourceLocation(Gunsmoke.MODID, "items/rounds/default/round");
                this.casing = new ResourceLocation(Gunsmoke.MODID, "items/rounds/default/casing");
                this.accessories = null;
                this.color = null;
            }

            private Render(int roundColor, ResourceLocation round, ResourceLocation casing, ResourceLocation accessories, ResourceLocation color) {
                this.roundColor = roundColor;
                this.round = round;
                this.casing = casing;
                this.accessories = accessories;
                this.color = color;
            }

            public static Render fromJson(JsonObject json) {
                int roundColor = Color.getIntFromRGB(255, 255, 255);
                ResourceLocation round = null;
                ResourceLocation casing = null;
                ResourceLocation accessories = null;
                ResourceLocation color = null;

                if (json.has("color")) {
                    JsonArray colorArray = json.getAsJsonArray("color");
                    roundColor = Color.getIntFromRGB(colorArray.get(0).getAsInt(), colorArray.get(1).getAsInt(), colorArray.get(2).getAsInt());
                }
                if (json.has("layers")) {
                    JsonObject layers = json.getAsJsonObject("layers");

                    if (layers.has("round")) {
                        round = new ResourceLocation(layers.get("round").getAsString());
                    }
                    if (layers.has("casing")) {
                        casing = new ResourceLocation(layers.get("casing").getAsString());
                    }
                    if (layers.has("accessories")) {
                        accessories = new ResourceLocation(layers.get("accessories").getAsString());
                    }
                    if (layers.has("color")) {
                        color = new ResourceLocation(layers.get("color").getAsString());
                    }
                }

                return new Render(roundColor, round, casing, accessories, color);
            }

            public int getRoundColor() {
                return roundColor;
            }

            public ResourceLocation getRound() {
                return round;
            }

            public ResourceLocation getCasing() {
                return casing;
            }

            public ResourceLocation getAccessories() {
                return accessories;
            }

            public ResourceLocation getColor() {
                return color;
            }
        }
    }

    public static class ItemRound implements Round {
        private final String caliber;
        private final ResourceLocation item;
        private final Damage damage;
        private final Properties properties;

        private ItemRound(String caliber, ResourceLocation item, Damage damage, Properties properties) {
            this.caliber = caliber;
            this.item = item;
            this.damage = damage;
            this.properties = properties;
        }

        public static ItemRound fromJson(JsonObject json) {
            String caliber = json.get("caliber").getAsString();
            ResourceLocation item = new ResourceLocation(json.get("item").getAsString());
            if (!roundItems.containsKey(item)) {
                roundItems.put(item, new ArrayList<>());
            }
            if (!roundItems.get(item).contains(caliber)) {
                roundItems.get(item).add(caliber);
            }
            Damage damage = new Damage();
            Properties properties = new Properties();

            if (json.has("damage")) {
                damage = Damage.fromJson(json.getAsJsonObject("damage"));
            }
            if (json.has("properties")) {
                properties = Properties.fromJson(json.getAsJsonObject("properties"));
            }

            return new ItemRound(caliber, item, damage, properties);
        }

        @Override
        public String getCaliber() {
            return caliber;
        }

        @Override
        public Damage getDamage() {
            return damage;
        }

        @Override
        public Properties getProperties() {
            return properties;
        }

        @Override
        public boolean hasRenderer() {
            return false;
        }

        public ResourceLocation getItemLocation() {
            return item;
        }

        public Item getItem() {
            try {
                return ForgeRegistries.ITEMS.getValue(item);
            } catch (Exception e) {
                return Items.AIR;
            }
        }
    }
}
