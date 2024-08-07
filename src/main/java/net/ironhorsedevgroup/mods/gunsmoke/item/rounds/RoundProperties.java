package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;

public class RoundProperties {
    private final int id;

    private final float damage;
    private boolean powder = false;
    private boolean gravity = true;
    private int life = 5;
    private float size = 1.0f;
    private double speed = 10.0;
    private int projectileAmount = 1;

    private float spreadMultiplier = 1;
    private int barrelDamage = 1;
    private int breachDamage = 1;
    private int coreDamage = 1;
    private int stockDamage = 1;

    private RoundTextureSources texture = null;

    public RoundProperties(int id, Double damage) {
        this.damage = damage.floatValue();
        this.id = id;
    }

    public RoundProperties(JsonObject json) {
        this.id = json.get("id").getAsInt();

        JsonObject properties = json.getAsJsonObject("properties");
        this.damage = properties.get("damage").getAsFloat();
        if (properties.has("powder")) {
            this.powder = properties.get("powder").getAsBoolean();
        }
        if (properties.has("gravity")) {
            this.gravity = properties.get("gravity").getAsBoolean();
        }
        if (properties.has("life")) {
            this.life = properties.get("life").getAsInt();
        }
        if (properties.has("size")) {
            this.size = properties.get("size").getAsFloat();
        }
        if (properties.has("speed")) {
            this.speed = properties.get("speed").getAsDouble();
        }
        if (properties.has("projectile_amount")) {
            this.projectileAmount = properties.get("projectile_amount").getAsInt();
        }

        if (json.has("gun_impacts")) {
            JsonObject impacts = json.getAsJsonObject("gun_impacts");
            if (impacts.has("spread_multiplier")) {
                this.spreadMultiplier = impacts.get("spread_multiplier").getAsFloat();
            }
            if (impacts.has("barrel_damage")) {
                this.barrelDamage = impacts.get("barrel_damage").getAsInt();
            }
            if (impacts.has("breach_damage")) {
                this.barrelDamage = impacts.get("breach_damage").getAsInt();
            }
            if (impacts.has("core_damage")) {
                this.barrelDamage = impacts.get("core_damage").getAsInt();
            }
            if (impacts.has("stock_damage")) {
                this.barrelDamage = impacts.get("stock_damage").getAsInt();
            }
        }

        if (json.has("textures")) {
            JsonObject textures = json.getAsJsonObject("textures");
            texture = new RoundTextureSources();
            if (textures.has("round")) {
                texture.addRound(textures.get("round").getAsString());
            }
            if (textures.has("casing")) {
                texture.addCasing(textures.get("casing").getAsString());
            }
            if (textures.has("color")) {
                JsonArray color = textures.getAsJsonArray("color");
                texture.addColor(color.get(0).getAsString(), Color.getIntFromRGB(color.get(1).getAsInt(), color.get(2).getAsInt(), color.get(3).getAsInt()));
            }
            if (textures.has("accessory")) {
                texture.addAccessory(textures.get("accessory").getAsString());
            }
        }
    }

    public int getId() {
        return this.id;
    }

    public Float getDamage() {
        return damage;
    }

    public RoundProperties setBarrelDamage(int barrelDamage) {
        this.barrelDamage = barrelDamage;
        return this;
    }

    public int getBarrelDamage() {
        return barrelDamage;
    }

    public RoundProperties setBreachDamage(int breachDamage) {
        this.breachDamage = breachDamage;
        return this;
    }

    public int getBreachDamage() {
        return breachDamage;
    }

    public RoundProperties setCoreDamage(int coreDamage) {
        this.coreDamage = coreDamage;
        return this;
    }

    public int getCoreDamage() {
        return coreDamage;
    }

    public RoundProperties setStockDamage(int stockDamage) {
        this.stockDamage = stockDamage;
        return this;
    }

    public int getStockDamage() {
        return stockDamage;
    }

    public RoundProperties setGunDamage(int damage) {
        return this.setBarrelDamage(damage)
                .setBreachDamage(damage)
                .setCoreDamage(damage)
                .setStockDamage(damage);
    }

    public RoundProperties setTexture(RoundTextureSources texture) {
        this.texture = texture;
        return this;
    }

    public RoundTextureSources getTexture() {
        return this.texture;
    }

    public RoundProperties setPowder(Boolean value) {
        this.powder = value;
        return this;
    }

    public Boolean getPowder() {
        return powder;
    }

    public RoundProperties setGravity(boolean gravity) {
        this.gravity = gravity;
        return this;
    }

    public boolean getGravity() {
        return gravity;
    }

    public RoundProperties setLife(int life) {
        this.life = life;
        return this;
    }

    public int getLife() {
        return life;
    }

    public RoundProperties setProjectileAmount(int projectileAmount) {
        this.projectileAmount = projectileAmount;
        return this;
    }

    public int getProjectileAmount() {
        return projectileAmount;
    }

    public RoundProperties setSize(Double size) {
        this.size = size.floatValue();
        return this;
    }

    public Float getSize() {
        return size;
    }

    public RoundProperties setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public double getSpeed() {
        return speed;
    }
}
