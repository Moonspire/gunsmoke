package net.ironhorsedevgroup.mods.gunsmoke.olditem;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.olditem.guns.GunMaterial;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.olditem.rounds.CaliberProperties;
import net.ironhorsedevgroup.mods.gunsmoke.olditem.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RoundItem extends Item {
    private final CaliberProperties caliber;

    public RoundItem(Properties properties, CaliberProperties caliber) {
        super(properties);
        this.caliber = caliber;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (RoundProperties round : this.caliber.getRounds()) {
                itemStack.add(
                        new NewRound(round.getId())
                                .bullet(new ResourceLocation(Gunsmoke.MODID, "lead"))
                                .casing(new ResourceLocation(Gunsmoke.MODID, "brass"))
                                .getItem(this)
                );
            }
        }
    }

    public static class NewRound {
        private final int id;
        private ResourceLocation bulletMaterial = null;
        private ResourceLocation casingMaterial = null;

        public NewRound(int id) {
            this.id = id;
        }

        public NewRound bullet(ResourceLocation material) {
            bulletMaterial = material;
            return this;
        }

        public NewRound casing(ResourceLocation material) {
            casingMaterial = material;
            return this;
        }

        public ItemStack getItem(Item round) {
            ItemStack retStack = new ItemStack(round);
            NBT.putIntTag(retStack, "CustomModelData", id);
            if (bulletMaterial != null) {
                NBT.putStringTag(retStack, "material_0", bulletMaterial.toString());
            }
            if (casingMaterial != null) {
                NBT.putStringTag(retStack, "material_1", casingMaterial.toString());
            }
            return retStack;
        }
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return I18n.get(this.getDescriptionId() + "." + NBT.getIntTag(itemStack, "CustomModelData"));
    }

    public static RoundProperties getRound(ItemStack itemStack) {
        if (itemStack.getItem() instanceof RoundItem roundItem) {
            return roundItem.getCaliber().getRound(NBT.getIntTag(itemStack, "CustomModelData"));
        }
        return null;
    }

    public static RoundProperties getModifiedRound(ItemStack itemStack) {
        MaterialUtils.Material bullet = MaterialUtils.getMaterial(new ResourceLocation(NBT.getStringTag(itemStack, "material_0")));
        MaterialUtils.Material casing = MaterialUtils.getMaterial(new ResourceLocation(NBT.getStringTag(itemStack, "material_1")));

        RoundProperties oldRound = getRound(itemStack);

        int id = oldRound.getId();

        Double damage = oldRound.getDamage().doubleValue();
        ResourceLocation texture = oldRound.getTexture();
        boolean gravity = oldRound.getGravity() || bullet.getProperties().getDensity() <= 0;
        int life = oldRound.getLife();
        Double size = oldRound.getSize().doubleValue();

        int projectiles = 1;
        if (oldRound.getProjectileAmount() > 0) {
            projectiles = (int) Math.round(100.0 / (float) oldRound.getProjectileAmount());
        }
        if (projectiles > 30) {
            projectiles = 30;
        }

        Double speed = oldRound.getSpeed();
        Boolean powder = oldRound.getPowder();

        int barrelDamage = 1;
        if (oldRound.getBarrelDamage() > 0) {
            barrelDamage = (int) Math.round(Math.pow(1.1, bullet.getProperties().getHardness()) / (float) (bullet.getProperties().getPurity() * 10));
            if (barrelDamage < 1) {
                barrelDamage = 1;
            }
        }

        int breachDamage = 1;
        if (oldRound.getBreachDamage() > 0); {
            breachDamage = (int) Math.round((oldRound.getBreachDamage() * 1000.0) / (casing.getProperties().getPurity() * casing.getProperties().getDensity()));
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

    public CaliberProperties getCaliber() {
        return caliber;
    }
}
