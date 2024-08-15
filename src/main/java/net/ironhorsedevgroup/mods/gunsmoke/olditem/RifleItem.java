package net.ironhorsedevgroup.mods.gunsmoke.olditem;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.olditem.guns.GunProperties;
import net.ironhorsedevgroup.mods.gunsmoke.olditem.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RifleItem extends GunItem {
    private final GunProperties gunProperties;
    public RifleItem(Properties properties, Integer reloadWait) {
        super(properties);
        gunProperties = new GunProperties(super.getGun(), reloadWait);
    }

    @Override
    public Gun getModifiedGun(ItemStack itemStack) {
        return gunProperties.getGun(itemStack);
    }

    public void damageGun(ItemStack itemStack) {
        gunProperties.damageGun(itemStack);
    }

    public ItemStack setMaterials(GunsmokeMaterials stock, GunsmokeMaterials barrel, GunsmokeMaterials core, GunsmokeMaterials breach) {
        ItemStack gunItem = new ItemStack(this);
        NBT.putStringTag(gunItem, "material_1", stock.getSerializedName());
        NBT.putStringTag(gunItem, "material_2", barrel.getSerializedName());
        NBT.putStringTag(gunItem, "material_3", core.getSerializedName());
        NBT.putStringTag(gunItem, "material_4", breach.getSerializedName());
        return gunItem;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            ItemStack gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunsmokeMaterials.SPRUCE.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunsmokeMaterials.WROUGHT_IRON.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunsmokeMaterials.BRASS.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunsmokeMaterials.WROUGHT_IRON.getSerializedName());
            loadRound(gunItem);
            itemStack.add(gunItem);

            gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunsmokeMaterials.SMOGSTEM.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunsmokeMaterials.CLOGGRUM.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunsmokeMaterials.RAW_IRON.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunsmokeMaterials.CLOGGRUM.getSerializedName());
            loadRound(gunItem);
            itemStack.add(gunItem);

            gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunsmokeMaterials.CRIMSON.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunsmokeMaterials.PIGSTEEL.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunsmokeMaterials.HOGSGOLD.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunsmokeMaterials.PIGSTEEL.getSerializedName());
            loadRound(gunItem);
            itemStack.add(gunItem);
        }
    }

    public Integer getReload(ItemStack itemStack) {
        return gunProperties.getReloadWait(itemStack);
    }

    public RifleItem loadRound(RoundProperties round) {
        gunProperties.loadRound(round);
        return this;
    }

    private void loadRound(ItemStack gunItem) {
        Item projectile = ForgeRegistries.ITEMS.getValue(this.getGun().getProjectile().getItem());
        int ammo = this.getGun().getGeneral().getMaxAmmo();
        if (projectile instanceof RoundItem roundItem) {
            RoundProperties round = roundItem.getCaliber().getRound(0);
            for (int i = 0; i < ammo; i++) {
                loadRound(round);
            }
        }
        NBT.putIntTag(gunItem, "AmmoCount", ammo);
    }

    public RifleItem removeRound() {
        gunProperties.removeRound();
        return this;
    }

    public RifleItem fireRound(ItemStack gunItem) {
        gunProperties.fireRound(gunItem);
        return this;
    }
}
