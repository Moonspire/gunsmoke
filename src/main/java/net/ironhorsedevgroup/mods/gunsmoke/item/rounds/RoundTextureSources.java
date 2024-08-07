package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import net.minecraft.resources.ResourceLocation;

public class RoundTextureSources {
    private ResourceLocation casing = null;
    private ResourceLocation round = null;
    private ResourceLocation color = null;
    private int rgb = 0;
    private ResourceLocation accessory = null;

    public RoundTextureSources() {}

    public RoundTextureSources addCasing(String location) {
        casing = new ResourceLocation (location);
        return this;
    }

    public ResourceLocation getCasing() {
        return casing;
    }

    public boolean renderCasing() {
        return casing != null;
    }

    public RoundTextureSources addRound(String location) {
        round = new ResourceLocation (location);
        return this;
    }

    public ResourceLocation getRound() {
        return round;
    }

    public boolean renderRound() {
        return round != null;
    }

    public RoundTextureSources addColor(String location, int color) {
        this.color = new ResourceLocation (location);
        rgb = color;
        return this;
    }

    public ResourceLocation getColor() {
        return color;
    }

    public int getRGB() {
        return rgb;
    }

    public boolean renderColor() {
        return color != null;
    }

    public RoundTextureSources addAccessory(String location) {
        accessory = new ResourceLocation (location);
        return this;
    }

    public ResourceLocation getAccessory() {
        return accessory;
    }

    public boolean renderAccessory() {
        return accessory != null;
    }
}
