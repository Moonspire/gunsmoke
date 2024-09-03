package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.network.GunsmokeMessages;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundItemPacket;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundRenderPacket;
import net.ironhorsedevgroup.mods.toolshed.content_packs.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundUtils {
    private static final Map<String, Map<String, Round>> rounds = new HashMap<>();
    private static final Map<ResourceLocation, List<String>> roundItems = new HashMap<>();

    public static void loadRounds(List<ResourceLocation> rounds, MinecraftServer server) {
        for (ResourceLocation round : rounds) {
            loadRound(round, server);
        }
    }

    public static void loadRound(ResourceLocation location, MinecraftServer server) {
        Round round = Round.fromJson(DataLoader.loadJson(location, server));
        String[] strippedPath = location.getPath().split("/");
        location = new ResourceLocation(round.getCaliber(), strippedPath[strippedPath.length - 1]);
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
            return getRound(NBT.getLocationTag(itemStack, "round"));
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

        default DynamicRound.Render getRender() {
            return new DynamicRound.Render();
        }

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
            Damage damage;
            Properties properties;
            Render render;

            if (json.has("damage")) {
                damage = Damage.fromJson(json.getAsJsonObject("damage"));
            } else {
                damage = new Damage();
            }
            if (json.has("properties")) {
                properties = Properties.fromJson(json.getAsJsonObject("properties"));
            } else {
                properties = new Properties();
            }
            if (json.has("render")) {
                render = Render.fromJson(json.getAsJsonObject("render"));
            } else {
                render = new Render();
            }

            return new DynamicRound(caliber, damage, properties, render);
        }

        public static Round fromPacket(RoundRenderPacket packet) {
            String caliber = packet.location.getNamespace();
            Damage damage = new Damage();
            Properties properties = new Properties();
            Render render = Render.fromPacket(packet);

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
            private final ResourceLocation model;

            public Render() {
                this.roundColor = Color.getIntFromRGB(255, 255, 255);
                this.model = new ResourceLocation(Gunsmoke.MODID, "gunsmoke:rounds/simple_pointed");
            }

            private Render(int roundColor, ResourceLocation model) {
                this.roundColor = roundColor;
                this.model = model;
            }

            public static Render fromJson(JsonObject json) {
                int roundColor = Color.getIntFromRGB(255, 255, 255);
                ResourceLocation model = null;

                if (json.has("color")) {
                    JsonArray colorArray = json.getAsJsonArray("color");
                    roundColor = Color.getIntFromRGB(colorArray.get(0).getAsInt(), colorArray.get(1).getAsInt(), colorArray.get(2).getAsInt());
                }
                if (json.has("model")) {
                    model = new ResourceLocation(json.get("model").getAsString());
                }

                return new Render(roundColor, model);
            }

            public static Render fromPacket(RoundRenderPacket packet) {
                ResourceLocation model = packet.model;
                int color = packet.color;

                return new Render(color, model);
            }

            public int getColor() {
                return roundColor;
            }

            public ResourceLocation getModel() {
                return model;
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
            Damage damage;
            Properties properties;

            if (json.has("damage")) {
                damage = Damage.fromJson(json.getAsJsonObject("damage"));
            } else {
                damage = new Damage();
            }
            if (json.has("properties")) {
                properties = Properties.fromJson(json.getAsJsonObject("properties"));
            } else {
                properties = new Properties();
            }

            return new ItemRound(caliber, item, damage, properties);
        }

        public static ItemRound fromPacket(RoundItemPacket packet) {
            String caliber = packet.location.getNamespace();
            ResourceLocation item = packet.item;
            Damage damage = new Damage();
            Properties properties = new Properties();

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
