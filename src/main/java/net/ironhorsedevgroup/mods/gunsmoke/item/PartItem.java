package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.parts.Parts;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PartItem extends Item {
    public PartItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (ResourceLocation part : Parts.getAllParts().keySet()) {
                for (ResourceLocation material : Parts.getPart(part).getRender().getMaterials()) {
                    if (Materials.hasMaterial(material)) {
                        itemStack.add(getDefaultInstance(part, material));
                    }
                }
            }
        }
    }

    public static ItemStack addMaterial(ItemStack partItem, String material) {
        return addMaterial(partItem, new ResourceLocation(material));
    }

    public static ItemStack addMaterial(ItemStack partItem, ResourceLocation material) {
        if (partItem.getItem() instanceof RoundItem) {
            NBT.putLocationTag(partItem, "material", material);
        }
        return partItem;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return getDefaultInstance("gunsmoke:gun_parts");
    }

    public static ItemStack getDefaultInstance(String location) {
        return getDefaultInstance(new ResourceLocation(location));
    }

    public static ItemStack getDefaultInstance(ResourceLocation location) {
        return getDefaultInstance(location, new ResourceLocation("forge:brass"));
    }

    public static ItemStack getDefaultInstance(ResourceLocation location, ResourceLocation material) {
        ItemStack partItem = new ItemStack(GunsmokeItems.PART_ITEM.get());
        NBT.putLocationTag(partItem, "part", location);
        NBT.putLocationTag(partItem, "material", material);
        return partItem;
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        ResourceLocation location = new ResourceLocation(NBT.getStringTag(itemStack, "part"));
        return "dynamicItem.part." + location.getNamespace() + "." + location.getPath();
    }
}
