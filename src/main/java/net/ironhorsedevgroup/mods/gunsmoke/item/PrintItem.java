package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class PrintItem extends Item {
    public PrintItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return super.getDescriptionId() + "." + NBT.getStringTag(itemStack, "printType");
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (RegistryObject<Item> item : GunsmokeItems.REGISTRY.getEntries()) {
                if (item.get() instanceof RifleItem rifle && rifle.hasPrints()) {
                    ItemStack print = new ItemStack(GunsmokeItems.PRINTS.get());
                    NBT.putStringTag(print, "printType", item.getId().getPath());
                    itemStack.add(print);
                }
            }
            for (RegistryObject<Item> item : GunsmokeItems.CALIBERS.values()) {
                ItemStack print = new ItemStack(GunsmokeItems.PRINTS.get());
                NBT.putStringTag(print, "printType", item.getId().getPath());
                NBT.putIntTag(print, "CustomModelData", 1);
                itemStack.add(print);
            }
        }
    }
}
