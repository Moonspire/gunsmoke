package net.ironhorsedevgroup.mods.gunsmoke.item;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

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

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            ItemStack gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunMaterials.SPRUCE.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunMaterials.WROUGHT_IRON.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunMaterials.BRASS.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunMaterials.WROUGHT_IRON.getSerializedName());
            NBT.putIntTag(gunItem, "AmmoCount", this.getModifiedGun(gunItem).getGeneral().getMaxAmmo());
            itemStack.add(gunItem);

            gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunMaterials.SPRUCE.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunMaterials.WROUGHT_IRON.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunMaterials.BRASS.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunMaterials.WROUGHT_IRON.getSerializedName());
            NBT.putIntTag(gunItem, "AmmoCount", this.getModifiedGun(gunItem).getGeneral().getMaxAmmo());
            this.gunProperties.damageGun(gunItem, 200);
            itemStack.add(gunItem);

            gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunMaterials.SMOGSTEM.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunMaterials.CLOGGRUM.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunMaterials.RAW_IRON.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunMaterials.CLOGGRUM.getSerializedName());
            NBT.putIntTag(gunItem, "AmmoCount", this.getModifiedGun(gunItem).getGeneral().getMaxAmmo());
            itemStack.add(gunItem);

            gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunMaterials.CRIMSON.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunMaterials.PIGSTEEL.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunMaterials.HOGSGOLD.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunMaterials.PIGSTEEL.getSerializedName());
            NBT.putIntTag(gunItem, "AmmoCount", this.getModifiedGun(gunItem).getGeneral().getMaxAmmo());
            itemStack.add(gunItem);

            gunItem = new ItemStack(this);
            NBT.putStringTag(gunItem, "material_1", GunMaterials.SHULKER.getSerializedName());
            NBT.putStringTag(gunItem, "material_2", GunMaterials.LEAD.getSerializedName());
            NBT.putStringTag(gunItem, "material_3", GunMaterials.SHULKER.getSerializedName());
            NBT.putStringTag(gunItem, "material_4", GunMaterials.LEAD.getSerializedName());
            NBT.putIntTag(gunItem, "AmmoCount", this.getModifiedGun(gunItem).getGeneral().getMaxAmmo());
            itemStack.add(gunItem);
        }
    }

    public Integer getReload(ItemStack itemStack) {
        return gunProperties.getReloadWait(itemStack);
    }
}
