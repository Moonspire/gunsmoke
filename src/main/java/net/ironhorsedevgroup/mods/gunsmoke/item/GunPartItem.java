package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
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
            for (ResourceLocation location : Materials.getMaterials()) {
                itemStack.add(NBT.putLocationTag(new ItemStack(this), "material", location));
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return I18n.get(this.getDescriptionId() + "." + NBT.getStringTag(itemStack, "material"));
    }
}
