package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class MaterialRound implements Round {
    private final ResourceLocation location;
    private final Damage damage;
    private final Properties properties;
    private final ResourceLocation round;
    private final ResourceLocation casing;

    private MaterialRound(ResourceLocation location, Damage damage, Properties properties, ResourceLocation round, ResourceLocation casing) {
        this.location = location;
        this.damage = damage;
        this.properties = properties;
        this.round = round;
        this.casing = casing;
    }

    public static Round fromItemStack(ItemStack stack) {
        Round round = Rounds.getRound(stack);
        if (round instanceof DynamicRound) {
            String caliber = round.getCaliber();
            Damage damage = round.getDamage();
            Properties properties = round.getProperties();

            ResourceLocation projectile = NBT.getLocationTag(stack, "round");
            ResourceLocation casing = NBT.getLocationTag(stack, "casing");

            return new MaterialRound(round.getLocation(), damage, properties, projectile, casing);
        }
        return round;
    }

    public static Round fromTag(String tag, ItemStack stack) {
        String id = NBT.getStringTag(stack, tag + ".location");
        if (!Objects.equals(id, "")) {
            ResourceLocation location = new ResourceLocation(id);
            Round round = Rounds.getRound(location);
            if (round instanceof DynamicRound) {
                Damage damage = round.getDamage();
                Properties properties = round.getProperties();

                ResourceLocation projectile = NBT.getLocationTag(stack, tag + ".round");
                ResourceLocation casing = NBT.getLocationTag(stack, tag + ".casing");

                return new MaterialRound(location, damage, properties, projectile, casing);
            }
            return round;
        }
        return null;
    }

    @Override
    public void putTag(String tag, ItemStack stack) {
        NBT.putStringTag(stack, tag + ".location", location.toString());
        NBT.putLocationTag(stack, tag + ".round", round);
        NBT.putLocationTag(stack, tag + ".casing", casing);
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    @Override
    public String getCaliber() {
        return location.getNamespace();
    }

    @Override
    public Damage getDamage() {
        return damage.getModified(round, casing);
    }

    @Override
    public Properties getProperties() {
        return properties.getModified(round, casing);
    }

    @Override
    public boolean hasRenderer() {
        return false;
    }
}
