package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import net.minecraft.resources.ResourceLocation;

public class RoundProperties {
    private final int id;
    private final float damage;
    private int barrelDamage = 1;
    private int breachDamage = 1;
    private int coreDamage = 1;
    private int stockDamage = 1;
    private boolean gravity = true;
    private int life = 5;
    private float size = 1.0f;
    private double speed = 10.0;
    private int projectileAmount = 1;
    private boolean powder = false;
    private boolean caseless = false;
    private boolean round = true;
    private boolean accessories = false;
    private int specialColor = 0;
    private ResourceLocation texture = new ResourceLocation("gunsmoke", "items/rounds/default");

    public RoundProperties(int id, Double damage) {
        this.damage = damage.floatValue();
        this.id = id;
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

    public RoundProperties setTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public RoundProperties setTexture(String texture) {
        this.texture = new ResourceLocation(texture);
        return this;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public void setAccessories(boolean accessories) {
        this.accessories = accessories;
    }

    public boolean hasAccessories() {
        return accessories;
    }

    public RoundProperties setColor(int color) {
        this.specialColor = color;
        return this;
    }

    public int getColor() {
        return specialColor;
    }

    public boolean hasColor() {
        return !(specialColor == 0);
    }

    public RoundProperties setPowder(Boolean value) {
        this.powder = value;
        return this;
    }

    public Boolean getPowder() {
        return powder;
    }

    public RoundProperties setRoundRender(boolean round) {
        this.round = round;
        return this;
    }

    public boolean isRoundRendered() {
        return round;
    }

    public RoundProperties setCaseless(boolean caseless) {
        this.caseless = caseless;
        return this;
    }

    public boolean isCaseless() {
        return caseless;
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
