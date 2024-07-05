package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GunPartItem extends Item {
    public GunPartItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (GunMaterials material : GunMaterials.values()) {
                if (material != GunMaterials.NULL) {
                    itemStack.add(NBT.putStringTag(new ItemStack(this), "material", material.getSerializedName()));
                }
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return I18n.get(this.getDescriptionId() + "." + GunMaterials.getMaterial(itemStack).getName());
    }
}
