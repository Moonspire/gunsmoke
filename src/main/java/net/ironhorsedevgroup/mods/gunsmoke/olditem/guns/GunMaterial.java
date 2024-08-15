package net.ironhorsedevgroup.mods.gunsmoke.olditem.guns;

import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class GunMaterial {
    private final String name;
    private int color = Color.getIntFromRGB(255, 0, 255);
    private boolean flammable = false;
    private short density = 0; // kg/m3 / 100
    private short hardness = 0; // Rough Mohs Hardness * 10
    private short purity = 0; // Just vibes man
    private ResourceLocation casting_fluid = null;
    private boolean castable = false;
    private ResourceLocation crafting_item = null;

    public GunMaterial(String name) {
        this.name = name;
    }

    public static GunMaterial TinkersCastable(String name, int r, int g, int b) {
        return new GunMaterial(name).setCastingFluid("tconstruct:molten_" + name).setCastable(true).setColor(r, g, b);
    }

    public String getName() {
        return name;
    }

    public GunMaterial setColor(int r, int g, int b) {
        this.color = Color.getIntFromRGB(r, g, b);
        return this;
    }

    public int getColor() {
        return color;
    }

    public GunMaterial setFlammable(Boolean flammable) {
        this.flammable = flammable;
        return this;
    }

    public Boolean isFlammable() {
        return flammable;
    }

    public GunMaterial setDensity(Integer density) {
        this.density = density.shortValue();
        return this;
    }

    public Integer getDensity() {
        return (int)density;
    }

    public GunMaterial setHardness(Integer hardness) {
        this.hardness = hardness.shortValue();
        return this;
    }

    public Integer getHardness() {
        return (int)hardness;
    }

    public GunMaterial setPurity(Integer purity) {
        this.purity = purity.shortValue();
        return this;
    }

    public Integer getPurity() {
        return (int)purity;
    }

    public GunMaterial setCastingFluid(String location) {
        casting_fluid = new ResourceLocation(location);
        return this;
    }

    public GunMaterial setCastingFluid(ResourceLocation fluidLocation) {
        casting_fluid = fluidLocation;
        return this;
    }

    public ResourceLocation getCastingFluidID() {
        return casting_fluid;
    }

    public Fluid getCastingFluid() {
        if (casting_fluid != null) {
            if (ModList.get().isLoaded(casting_fluid.getNamespace())) {
                return ForgeRegistries.FLUIDS.getValue(casting_fluid);
            }
        }
        return null;
    }

    public GunMaterial setCastable(Boolean castable) {
        this.castable = castable;
        return this;
    }

    public Boolean isCastable() {
        return castable;
    }

    public GunMaterial setCraftingItem(String item) {
        crafting_item = new ResourceLocation(item);
        return this;
    }

    public GunMaterial setCraftingItem(ResourceLocation item) {
        crafting_item = item;
        return this;
    }

    public ResourceLocation getCraftingItemID() {
        return crafting_item;
    }

    public Item getCraftingItem() {
        if (crafting_item != null) {
            if (ModList.get().isLoaded(crafting_item.getNamespace())) {
                return ForgeRegistries.ITEMS.getValue(crafting_item);
            }
        }
        return null;
    }

    public Boolean isCraftable() {
        return crafting_item != null;
    }

    public Boolean isMaterial(String name) {
        return Objects.equals(this.name, name);
    }

    public Boolean isMaterial(GunMaterial material) {
        return Objects.equals(this, material);
    }
}
