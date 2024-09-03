package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundRenderPacket;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.minecraft.resources.ResourceLocation;

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

    default Render getRender() {
        return new Render();
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

        public Damage getModified(ResourceLocation round, ResourceLocation casing) {
            return new Damage(
                    this.entityDamage,
                    this.barrelDamage,
                    this.breachDamage,
                    this.coreDamage,
                    this.stockDamage
            );
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

        public Properties getModified(ResourceLocation round, ResourceLocation casing) {
            return new Properties(
                    this.damageDropoff,
                    this.hasGravity,
                    this.life,
                    this.projectiles,
                    this.size,
                    this.speed,
                    this.trailColor,
                    this.trailLength,
                    this.visible
            );
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

    class Render {
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