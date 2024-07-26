package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.ItemStack;

public class GunMakeup {
    private Integer barrelDamage = 0;
    private Integer breachDamage = 0;
    private Integer coreDamage = 0;
    private Integer stockDamage = 0;
    private Integer barrelDamagePermanent = 0;
    private Integer breachDamagePermanent = 0;
    private Integer coreDamagePermanent = 0;
    private Integer stockDamagePermanent = 0;
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
        barrelDamage = NBT.getIntTag(itemStack, "barrelDamage");
        barrelDamagePermanent = NBT.getIntTag(itemStack, "barrelDamagePermanent");
        breachDamage = NBT.getIntTag(itemStack, "breachDamage");
        breachDamagePermanent = NBT.getIntTag(itemStack, "breachDamagePermanent");
        coreDamage = NBT.getIntTag(itemStack, "coreDamage");
        coreDamagePermanent = NBT.getIntTag(itemStack, "coreDamagePermanent");
        stockDamage = NBT.getIntTag(itemStack, "stockDamage");
        stockDamagePermanent = NBT.getIntTag(itemStack, "stockDamagePermanent");
        stack = itemStack;
    }

    public GunMakeup() {
        stock = GunsmokeMaterials.NULL.getMaterial();
        barrel = GunsmokeMaterials.NULL.getMaterial();
        core = GunsmokeMaterials.NULL.getMaterial();
        breach = GunsmokeMaterials.NULL.getMaterial();
    }

    public Integer getBarrelDamage() {
        return barrelDamage;
    }

    public Integer getBarrelDamagePermanent() {
        return barrelDamagePermanent;
    }

    public Integer getBarrelTotalDamage() {
        return barrelDamage + barrelDamagePermanent;
    }

    public Integer getBreachDamage() {
        return breachDamage;
    }

    public Integer getBreachDamagePermanent() {
        return breachDamagePermanent;
    }

    public Integer getBreachTotalDamage() {
        return breachDamage + breachDamagePermanent;
    }


    public Integer getCoreDamage() {
        return coreDamage;
    }

    public Integer getCoreDamagePermanent() {
        return coreDamagePermanent;
    }

    public Integer getCoreTotalDamage() {
        return coreDamage + coreDamagePermanent;
    }


    public Integer getStockDamage() {
        return stockDamage;
    }

    public Integer getStockDamagePermanent() {
        return stockDamagePermanent;
    }

    public Integer getStockTotalDamage() {
        return stockDamage + stockDamagePermanent;
    }

    public void dealDamage(Integer damage) {
        syncValues();
        int barrelHealth = 1;
        if (!barrel.isFlamable()) {
            barrelHealth = barrel.getHardness() * (barrel.getPurity() / 10);
        }

        barrelDamage = barrelDamage + damage;
        if (barrelDamage > barrelHealth - barrelDamagePermanent) {
            Integer barrelOverage = barrelDamage - (barrelHealth - barrelDamagePermanent);
            barrelDamagePermanent = barrelDamagePermanent + barrelOverage;
            barrelDamage = breachDamage - barrelOverage;
        }
        if (barrelDamagePermanent > barrelHealth) {
            barrelDamage = 0;
            barrelDamagePermanent = barrelHealth;
        }
        System.out.println("[TWEGun Barrel] Health: " + barrelHealth + ", Temp: " + barrelDamage + ", Permanent: " + barrelDamagePermanent);

        int breachHealth = 1;
        if (!breach.isFlamable()) {
            breachHealth = breach.getHardness() * (breach.getPurity() / 15);
        }

        breachDamage = breachDamage + damage;
        if (breachDamage > breachHealth - breachDamagePermanent) {
            Integer breachOverage = breachDamage - (breachHealth - breachDamagePermanent);
            breachDamagePermanent = breachDamagePermanent + breachOverage;
            breachDamage = breachDamage - breachOverage;
        }
        if (breachDamagePermanent > breachHealth) {
            breachDamage = 0;
            breachDamagePermanent = breachHealth;
        }
        System.out.println("[TWEGun Breach] Health: " + breachHealth + ", Temp: " + breachDamage + ", Permanent: " + breachDamagePermanent);

        int coreHealth = 1;
        if (!core.isFlamable()) {
            coreHealth = core.getHardness() * (core.getPurity() / 10);
        }

        coreDamage = coreDamage + damage;
        if (coreDamage > coreHealth - coreDamagePermanent) {
            Integer coreOverage = coreDamage - (coreHealth - coreDamagePermanent);
            coreDamagePermanent = coreDamagePermanent + coreOverage;
            coreDamage = breachDamage - coreOverage;
        }
        if (coreDamagePermanent > coreHealth) {
            coreDamage = 0;
            coreDamagePermanent = coreHealth;
        }
        System.out.println("[TWEGun Core] Health: " + coreHealth + ", Temp: " + coreDamage + ", Permanent: " + coreDamagePermanent);

        int stockHealth = stock.getHardness() * stock.getPurity() * 10;
        if (stock.isFlamable()) {
            stockHealth = (int)(stockHealth / 1.25);
        }

        stockDamage = stockDamage + damage;
        if (stockDamage > stockHealth - stockDamagePermanent) {
            Integer stockOverage = stockDamage - (stockHealth - stockDamagePermanent);
            stockDamagePermanent = stockDamagePermanent + stockOverage;
            stockDamage = breachDamage - stockOverage;
        }
        if (stockDamagePermanent > stockHealth) {
            stockDamage = 0;
            stockDamagePermanent = stockHealth;
        }
        System.out.println("[TWEGun Stock] Health: " + stockHealth + ",  Temp: " + stockDamage + ", Permanent: " + stockDamagePermanent);
        System.out.println("Gun Shot & Damaged");
        putDamage();
    }

    public void dealDamage() {
        syncValues();
        // Barrel
        int barrelHealth = 1;
        if (!barrel.isFlamable()) {
            barrelHealth = barrel.getHardness() * (barrel.getPurity() / 10);
        }

        if (barrelDamage + barrelDamagePermanent < barrelHealth) {
            barrelDamage = barrelDamage + 1;
        } else {
            barrelDamagePermanent = barrelDamagePermanent + 1;
            if (barrelDamage > 0) {
                barrelDamage = barrelDamage - 1;
            }
        }
        System.out.println("[TWEGun Barrel] Health: " + barrelHealth + ", Temp: " + barrelDamage + ", Permanent: " + barrelDamagePermanent);

        // Breach
        int breachHealth = 1;
        if (!breach.isFlamable()) {
            breachHealth = breach.getHardness() * (breach.getPurity() / 15);
        }

        if (breachDamage + breachDamagePermanent < breachHealth) {
            breachDamage = breachDamage + 1;
        } else {
            breachDamagePermanent = breachDamagePermanent + 1;
            if (breachDamage > 0) {
                breachDamage = breachDamage - 1;
            }
        }
        System.out.println("[TWEGun Breach] Health: " + breachHealth + ", Temp: " + breachDamage + ", Permanent: " + breachDamagePermanent);

        // Core
        int coreHealth = 1;
        if (!core.isFlamable()) {
            coreHealth = core.getHardness() * (core.getPurity() / 10);
        }

        if (coreDamage + coreDamagePermanent < coreHealth) {
            coreDamage = coreDamage + 1;
        } else {
            coreDamagePermanent = coreDamagePermanent + 1;
            if (coreDamage > 0) {
                coreDamage = coreDamage - 1;
            }
        }
        System.out.println("[TWEGun Core] Health: " + coreHealth + ", Temp: " + coreDamage + ", Permanent: " + coreDamagePermanent);

        // Stock
        int stockHealth = stock.getHardness() * stock.getPurity() * 10;
        if (stock.isFlamable()) {
            stockHealth = (int)(stockHealth / 1.25);
        }

        if (stockDamage + stockDamagePermanent < stockHealth) {
            stockDamage = stockDamage + 1;
        } else {
            stockDamagePermanent = stockDamagePermanent + 1;
            if (stockDamage > 0) {
                stockDamage = stockDamage - 1;
            }
        }
        System.out.println("[TWEGun Stock] Health: " + stockHealth + ",  Temp: " + stockDamage + ", Permanent: " + stockDamagePermanent);
        System.out.println("Gun Shot & Damaged");
        putDamage();
    }

    private void syncValues() {
        stock = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_1"));
        barrel = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_2"));
        core = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_3"));
        breach = GunsmokeMaterials.getMaterial(NBT.getStringTag(stack, "material_4"));
        barrelDamage = NBT.getIntTag(stack, "barrelDamage");
        barrelDamagePermanent = NBT.getIntTag(stack, "barrelDamagePermanent");
        breachDamage = NBT.getIntTag(stack, "breachDamage");
        breachDamagePermanent = NBT.getIntTag(stack, "breachDamagePermanent");
        coreDamage = NBT.getIntTag(stack, "coreDamage");
        coreDamagePermanent = NBT.getIntTag(stack, "coreDamagePermanent");
        stockDamage = NBT.getIntTag(stack, "stockDamage");
        stockDamagePermanent = NBT.getIntTag(stack, "stockDamagePermanent");
    }

    private void putDamage() {
        NBT.putIntTag(stack, "barrelDamage", barrelDamage);
        NBT.putIntTag(stack, "barrelDamagePermanent", barrelDamagePermanent);
        NBT.putIntTag(stack, "breachDamage", breachDamage);
        NBT.putIntTag(stack, "breachDamagePermanent", breachDamagePermanent);
        NBT.putIntTag(stack, "coreDamage", coreDamage);
        NBT.putIntTag(stack, "coreDamagePermanent", coreDamagePermanent);
        NBT.putIntTag(stack, "stockDamage", stockDamage);
        NBT.putIntTag(stack, "stockDamagePermanent", stockDamagePermanent);
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
                barrelDamage == gunMakeup.getBarrelDamage() &&
                barrelDamagePermanent == gunMakeup.getBarrelDamagePermanent() &&
                breach == gunMakeup.getBreach() &&
                breachDamage == gunMakeup.getBreachDamage() &&
                breachDamagePermanent == gunMakeup.getBreachDamagePermanent() &&
                core == gunMakeup.getCore() &&
                coreDamage == gunMakeup.getCoreDamage() &&
                coreDamagePermanent == gunMakeup.getCoreDamagePermanent() &&
                stock == gunMakeup.getStock() &&
                stockDamage == gunMakeup.getStockDamage() &&
                stockDamagePermanent == gunMakeup.getStockDamagePermanent()
                ;
    }
}
