package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class GunMakeup {
    private Damage barrelDamage = null;
    private Damage breachDamage = null;
    private Damage coreDamage = null;
    private Damage stockDamage = null;
    private GunMaterial barrel;
    private GunMaterial breach;
    private GunMaterial core;
    private GunMaterial stock;
    private ItemStack stack;

    public GunMakeup(ItemStack itemStack) {
        stock = GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_1"));
        barrel = GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_2"));
        core = GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_3"));
        breach = GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_4"));
        stack = itemStack;
    }

    public GunMakeup() {
        stock = GunsmokeMaterials.NULL.getMaterial();
        barrel = GunsmokeMaterials.NULL.getMaterial();
        core = GunsmokeMaterials.NULL.getMaterial();
        breach = GunsmokeMaterials.NULL.getMaterial();
    }

    public Integer getBarrelDamage() {
        syncValues();
        return barrelDamage.getDamage();
    }

    public Damage getBarrelDamageObject() {
        syncValues();
        return barrelDamage;
    }

    public Integer getBarrelDamagePermanent() {
        syncValues();
        return barrelDamage.getPermDamage();
    }

    public Integer getBarrelTotalDamage() {
        syncValues();
        return barrelDamage.getTotDamage();
    }

    public Integer getBreachDamage() {
        syncValues();
        return breachDamage.getDamage();
    }

    public Damage getBreachDamageObject() {
        syncValues();
        return breachDamage;
    }

    public Integer getBreachDamagePermanent() {
        syncValues();
        return breachDamage.getPermDamage();
    }

    public Integer getBreachTotalDamage() {
        syncValues();
        return breachDamage.getTotDamage();
    }


    public Integer getCoreDamage() {
        syncValues();
        return coreDamage.getDamage();
    }

    public Damage getCoreDamageObject() {
        syncValues();
        return coreDamage;
    }

    public Integer getCoreDamagePermanent() {
        syncValues();
        return coreDamage.getPermDamage();
    }

    public Integer getCoreTotalDamage() {
        syncValues();
        return coreDamage.getTotDamage();
    }


    public Integer getStockDamage() {
        syncValues();
        return stockDamage.getDamage();
    }

    public Damage getStockDamageObject() {
        syncValues();
        return stockDamage;
    }

    public Integer getStockDamagePermanent() {
        syncValues();
        return stockDamage.getPermDamage();
    }

    public Integer getStockTotalDamage() {
        syncValues();
        return stockDamage.getTotDamage();
    }

    public static class Damage {
        private static final int maxTreatment = 20;
        private final int maxHealth;
        private int totDamage;
        private int permDamage;
        private int treated;

        public Damage(int maxHealth) {
            this.maxHealth = maxHealth;
        }

        public static Damage newBarrel(GunMaterial material) {
            int barrelHealth = 1;
            if (!material.isFlammable()) {
                barrelHealth = material.getHardness() * (material.getPurity() / 10);
            }
            return new Damage(barrelHealth);
        }

        public static Damage newBreach(GunMaterial material) {
            int breachHealth = 1;
            if (!material.isFlammable()) {
                breachHealth = material.getHardness() * (material.getPurity() / 15);
            }
            return new Damage(breachHealth);
        }

        public static Damage newCore(GunMaterial material) {
            int coreHealth = 1;
            if (!material.isFlammable()) {
                coreHealth = material.getHardness() * (material.getPurity() / 10);
            }
            return new Damage(coreHealth);
        }

        public static Damage newStock(GunMaterial material) {
            int stockHealth = material.getHardness() * material.getPurity() * 10;
            if (material.isFlammable()) {
                stockHealth = (int)(stockHealth / 1.25);
            }
            return new Damage(stockHealth);
        }

        public int getMaxHealth() {
            return maxHealth;
        }

        public Damage damage(int damage) {
            if (damage > 0) {
                treated = treated - damage;
            }
            if (treated < 0) {
                totDamage = totDamage + Math.abs(treated);
                treated = 0;
            } else if (damage < 0) {
                totDamage = totDamage + damage;
            }
            if (totDamage > maxHealth) {
                permDamage = permDamage + (totDamage - maxHealth);
                totDamage = maxHealth;
            }
            if (permDamage > maxHealth) {
                permDamage = maxHealth;
            }
            if (totDamage < permDamage) {
                totDamage = permDamage;
            }
            return this;
        }

        public Damage repair(int health) {
            totDamage = totDamage - health;
            if (totDamage < permDamage) {
                totDamage = permDamage;
            }
            return this;
        }

        public Damage treat(int health) {
            treated = treated + health;
            if (treated > maxTreatment) {
                treated = maxTreatment;
            }
            return this;
        }

        public int getDamage() {
            return totDamage - permDamage;
        }

        public int getPermDamage() {
            return permDamage;
        }

        public int getTotDamage() {
            return totDamage;
        }

        public int getTreated() {
            return treated;
        }
    }

    public void dealBarrelDamage(Integer damage) {
        dealBarrelDamage(damage, true);
    }

    public void dealBarrelDamage(Integer damage, boolean sync) {
        if (sync) {
            syncValues();
        } else if (Objects.equals(barrelDamage, null)) {
            barrelDamage = Damage.newBarrel(barrel);
        }

        barrelDamage.damage(damage);

        System.out.println(
                "[TWEGun Barrel] Health: " + barrelDamage.getMaxHealth()
                        + ", Treatment: " + barrelDamage.getTreated()
                        + ", Total Damage: " + barrelDamage.getTotDamage()
                        + ", Permanent: " + barrelDamage.getPermDamage());
        if (sync) {
            putDamage();
        }
    }

    public void dealBreachDamage(Integer damage) {
        dealBreachDamage(damage, true);
    }

    public void dealBreachDamage(Integer damage, boolean sync) {
        if (sync) {
            syncValues();
        } else if (Objects.equals(breachDamage, null)) {
            breachDamage = Damage.newBreach(breach);
        }

        breachDamage.damage(damage);

        System.out.println(
                "[TWEGun Breach] Health: " + breachDamage.getMaxHealth()
                        + ", Treatment: " + breachDamage.getTreated()
                        + ", Total Damage: " + breachDamage.getTotDamage()
                        + ", Permanent: " + breachDamage.getPermDamage());
        if (sync) {
            putDamage();
        }
    }

    public void dealCoreDamage(Integer damage) {
        dealBreachDamage(damage, true);
    }

    public void dealCoreDamage(Integer damage, boolean sync) {
        if (sync) {
            syncValues();
        } else if (Objects.equals(coreDamage, null)) {
            coreDamage = Damage.newCore(core);
        }

        coreDamage.damage(damage);

        System.out.println(
                "[TWEGun Core] Health: " + coreDamage.getMaxHealth()
                        + ", Treatment: " + coreDamage.getTreated()
                        + ", Total Damage: " + coreDamage.getTotDamage()
                        + ", Permanent: " + coreDamage.getPermDamage());
        if (sync) {
            putDamage();
        }
    }

    public void dealStockDamage(Integer damage) {
        dealStockDamage(damage, true);
    }

    public void dealStockDamage(Integer damage, boolean sync) {
        if (sync) {
            syncValues();
        } else if (Objects.equals(stockDamage, null)) {
            stockDamage = Damage.newStock(stock);
        }

        stockDamage.damage(damage);

        System.out.println(
                "[TWEGun Stock] Health: " + stockDamage.getMaxHealth()
                        + ", Treatment: " + stockDamage.getTreated()
                        + ", Total Damage: " + stockDamage.getTotDamage()
                        + ", Permanent: " + stockDamage.getPermDamage());
        if (sync) {
            putDamage();
        }
    }

    public void dealDamage(RoundProperties round) {
        syncValues();
        dealBarrelDamage(round.getBarrelDamage(), false);
        dealBreachDamage(round.getBreachDamage(), false);
        dealCoreDamage(round.getCoreDamage(), false);
        dealStockDamage(round.getStockDamage(), false);
        putDamage();
        System.out.println("Gun Shot & Damaged Based On Round");

    }

    public void dealDamage(Integer damage) {
        syncValues();
        dealBarrelDamage(damage, false);
        dealBreachDamage(damage, false);
        dealCoreDamage(damage, false);
        dealStockDamage(damage, false);
        putDamage();
        System.out.println("Gun Shot & Damaged");
    }

    public void dealDamage() {
        dealDamage(1);
    }

    private void syncValues() {
        stock = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_1"));
        barrel = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_2"));
        core = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_3"));
        breach = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_4"));

        barrelDamage = Damage.newBarrel(barrel);
        barrelDamage.damage(barrelDamage.getMaxHealth() + NBT.getIntTag(stack, "barrelDamagePermanent"))
                .repair(barrelDamage.getMaxHealth() - NBT.getIntTag(stack, "barrelDamage"))
                .treat(NBT.getIntTag(stack, "barrelTreatment"));

        breachDamage = Damage.newBreach(breach);
        breachDamage.damage(breachDamage.getMaxHealth() + NBT.getIntTag(stack, "breachDamagePermanent"))
                .repair(breachDamage.getMaxHealth() - NBT.getIntTag(stack, "breachDamage"))
                .treat(NBT.getIntTag(stack, "breachTreatment"));

        coreDamage = Damage.newCore(core);
        coreDamage.damage(coreDamage.getMaxHealth() + NBT.getIntTag(stack, "coreDamagePermanent"))
                .repair(coreDamage.getMaxHealth() - NBT.getIntTag(stack, "coreDamage"))
                .treat(NBT.getIntTag(stack, "coreTreatment"));

        stockDamage = Damage.newStock(stock);
        stockDamage.damage(stockDamage.getMaxHealth() + NBT.getIntTag(stack, "stockDamagePermanent"))
                .repair(stockDamage.getMaxHealth() - NBT.getIntTag(stack, "stockDamage"))
                .treat(NBT.getIntTag(stack, "stockTreatment"));
    }

    private void putDamage() {
        NBT.putIntTag(stack, "barrelTreatment", barrelDamage.getTreated());
        NBT.putIntTag(stack, "barrelDamage", barrelDamage.getDamage());
        NBT.putIntTag(stack, "barrelDamagePermanent", barrelDamage.getPermDamage());

        NBT.putIntTag(stack, "breachTreatment", breachDamage.getTreated());
        NBT.putIntTag(stack, "breachDamage", breachDamage.getDamage());
        NBT.putIntTag(stack, "breachDamagePermanent", breachDamage.getPermDamage());

        NBT.putIntTag(stack, "coreTreatment", coreDamage.getTreated());
        NBT.putIntTag(stack, "coreDamage", coreDamage.getDamage());
        NBT.putIntTag(stack, "coreDamagePermanent", coreDamage.getPermDamage());

        NBT.putIntTag(stack, "stockTreatment", stockDamage.getTreated());
        NBT.putIntTag(stack, "stockDamage", stockDamage.getDamage());
        NBT.putIntTag(stack, "stockDamagePermanent", stockDamage.getPermDamage());
    }

    public GunMaterial getBarrel() {
        return barrel;
    }

    public GunMaterial getBreach() {
        return breach;
    }

    public GunMaterial getCore() {
        return core;
    }

    public GunMaterial getStock() {
        return stock;
    }

    public Boolean is(GunMakeup gunMakeup) {
        return
                barrel == gunMakeup.getBarrel() &&
                barrelDamage == gunMakeup.getBarrelDamageObject() &&
                breach == gunMakeup.getBreach() &&
                breachDamage == gunMakeup.getBreachDamageObject() &&
                core == gunMakeup.getCore() &&
                coreDamage == gunMakeup.getCoreDamageObject() &&
                stock == gunMakeup.getStock() &&
                stockDamage == gunMakeup.getStockDamageObject()
                ;
    }
}
