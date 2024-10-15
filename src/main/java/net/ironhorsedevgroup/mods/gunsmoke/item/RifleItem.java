package net.ironhorsedevgroup.mods.gunsmoke.item;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunProperties;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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

    public ItemStack setMaterials(String stock, String barrel, String core, String breach) {
        return setMaterials(new ResourceLocation(stock), new ResourceLocation(barrel), new ResourceLocation(core), new ResourceLocation(breach));
    }

    public ItemStack setMaterials(ResourceLocation stock, ResourceLocation barrel, ResourceLocation core, ResourceLocation breach) {
        ItemStack gunItem = new ItemStack(this);
        NBT.putLocationTag(gunItem, "material_1", stock);
        NBT.putLocationTag(gunItem, "material_2", barrel);
        NBT.putLocationTag(gunItem, "material_3", core);
        NBT.putLocationTag(gunItem, "material_4", breach);
        return gunItem;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            ItemStack gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", "minecraft:spruce");
            NBT.putStringTag(gunItem, "material_2", "minecraft:iron");
            NBT.putStringTag(gunItem, "material_3", "forge:brass");
            NBT.putStringTag(gunItem, "material_4", "minecraft:iron");
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
        if (projectile instanceof RoundItem) {
            RoundProperties round = gunProperties.getChamberedRound();
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
