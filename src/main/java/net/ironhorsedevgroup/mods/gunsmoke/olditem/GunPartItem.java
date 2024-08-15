package net.ironhorsedevgroup.mods.gunsmoke.olditem;

import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GunPartItem extends Item {
    public GunPartItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (GunsmokeMaterials material : GunsmokeMaterials.values()) {
                if (material != GunsmokeMaterials.NULL) {
                    itemStack.add(NBT.putStringTag(new ItemStack(this), "material", material.getSerializedName()));
                }
            }
        }
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack itemStack) {
        return I18n.get(this.getDescriptionId() + "." + GunsmokeMaterials.getMaterial(itemStack).getName());
    }
}
