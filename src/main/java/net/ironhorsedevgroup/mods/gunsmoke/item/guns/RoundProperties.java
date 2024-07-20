package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.minecraft.resources.ResourceLocation;

public class RoundProperties {
    private final int id;
    private final double damage;
    private boolean gravity = true;
    private int life = 5;
    private double size = 1.0;
    private double speed = 10.0;
    private int projectileAmount = 1;
    private boolean powder = false;
    private ResourceLocation texture = new ResourceLocation("gunsmoke", "textures/items/rounds/default");

    public RoundProperties(int id, Double damage) {
        this.damage = damage;
        this.id = id;
    }

    public int getId() {
        return this.id;
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

    public double getDamage() {
        return damage;
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

    public RoundProperties setSize(double size) {
        this.size = size;
        return this;
    }

    public double getSize() {
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
