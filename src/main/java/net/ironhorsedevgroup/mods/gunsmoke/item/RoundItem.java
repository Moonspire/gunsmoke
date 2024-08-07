package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterial;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundTextureSources;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RoundItem extends Item {
    private final List<RoundProperties> caliber;

    public RoundItem(Properties properties, List<RoundProperties> caliber) {
        super(properties);
        this.caliber = caliber;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (RoundProperties round : this.caliber) {
                itemStack.add(
                        new NewRound(round.getId())
                                .bullet(GunsmokeMaterials.LEAD)
                                .casing(GunsmokeMaterials.BRASS)
                                .getItem(this)
                );
            }
        }
    }

    public static class NewRound {
        private final int id;
        private GunsmokeMaterials bullet = GunsmokeMaterials.NULL;
        private GunsmokeMaterials casing = GunsmokeMaterials.NULL;

        public NewRound(int id) {
            this.id = id;
        }

        public NewRound bullet(GunsmokeMaterials material) {
            bullet = material;
            return this;
        }

        public NewRound casing(GunsmokeMaterials material) {
            casing = material;
            return this;
        }

        public ItemStack getItem(Item round) {
            ItemStack retStack = new ItemStack(round);
            NBT.putIntTag(retStack, "CustomModelData", id);
            NBT.putStringTag(retStack, "material_0", bullet.getSerializedName());
            NBT.putStringTag(retStack, "material_1", casing.getSerializedName());
            return retStack;
        }
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        int id = NBT.getIntTag(itemStack, "CustomModelData");
        if (id == 0) {
            return super.getDescriptionId(itemStack);
        }
        return this.getDescriptionId() + "." + id;
    }

    public List<RoundProperties> getCaliber() {
        return this.caliber;
    }

    public static List<RoundProperties> getCaliber(ItemStack itemStack) {
        if (itemStack.getItem() instanceof RoundItem round) {
            return round.getCaliber();
        }
        return null;
    }

    public static List<RoundProperties> getCaliber(String name) {
        if (GunsmokeItems.CALIBERS.containsKey(name)) {
            if (GunsmokeItems.CALIBERS.get(name).get() instanceof RoundItem round) {
                return round.getCaliber();
            }
        }
        return null;
    }

    public static RoundProperties getRound(ItemStack itemStack) {
        if (itemStack.getItem() instanceof RoundItem roundItem) {
            return roundItem.caliber.get(NBT.getIntTag(itemStack, "CustomModelData"));
        }
        return null;
    }

    public static RoundProperties getModifiedRound(ItemStack itemStack) {
        GunMaterial bullet = GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_0"));
        GunMaterial casing = GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_1"));

        RoundProperties oldRound = getRound(itemStack);

        int id = oldRound.getId();

        Double damage = oldRound.getDamage().doubleValue();
        RoundTextureSources texture = oldRound.getTexture();
        boolean gravity = oldRound.getGravity() && bullet.getDensity() > 0;
        int life = oldRound.getLife();
        Double size = oldRound.getSize().doubleValue();

        int projectiles = oldRound.getProjectileAmount();
        if (projectiles > 30) {
            projectiles = 30;
        }

        Double speed = oldRound.getSpeed();
        Boolean powder = oldRound.getPowder();

        int barrelDamage = 1;
        if (oldRound.getBarrelDamage() > 0) {
            barrelDamage = (int) Math.round(Math.pow(1.1, bullet.getHardness()) / (float) (bullet.getPurity() * 10));
            if (barrelDamage < 1) {
                barrelDamage = 1;
            }
        }

        int breachDamage = 1;
        if (oldRound.getBreachDamage() > 0); {
            breachDamage = (int) Math.round((oldRound.getBreachDamage() * 1000.0) / (casing.getPurity() * casing.getDensity()));
            if (breachDamage < 1) {
                breachDamage = 1;
            }
        }
        if (powder) {
            breachDamage = breachDamage * 2;
            barrelDamage = barrelDamage * 2;
        }

        int coreDamage = (int) Math.ceil(breachDamage / 2.0);
        int stockDamage = oldRound.getStockDamage();

        return new RoundProperties(id, damage)
                .setTexture(texture)
                .setGravity(gravity)
                .setLife(life)
                .setSize(size)
                .setProjectileAmount(projectiles)
                .setSpeed(speed)

                .setPowder(powder)
                .setBarrelDamage(barrelDamage)
                .setBreachDamage(breachDamage)
                .setCoreDamage(coreDamage)
                .setStockDamage(stockDamage);
    }
}
