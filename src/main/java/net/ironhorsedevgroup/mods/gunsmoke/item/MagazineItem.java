package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.Magazine;
import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.Magazines;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MagazineItem extends Item {
    public MagazineItem(Properties properties) {
        super(properties);
    }

    public static ItemStack getFromMagazine(String location) {
        return getFromMagazine(new ResourceLocation(location));
    }

    public static ItemStack getFromMagazine(ResourceLocation location) {
        Magazine magazine = Magazines.getMagazine(location);
        ItemStack magItem = new ItemStack(GunsmokeItems.ROUND_ITEM.get());
        NBT.putLocationTag(magItem, "magazine", location);
        return magItem;
    }

    public static ItemStack addMaterials(ItemStack magazineItem, String round, String casing) {
        return addMaterials(magazineItem, new ResourceLocation(round), new ResourceLocation(casing));
    }

    public static ItemStack addMaterials(ItemStack magazineItem, ResourceLocation round, ResourceLocation casing) {
        if (magazineItem.getItem() instanceof RoundItem) {
            NBT.putStringTag(magazineItem, "round", round.toString());
            NBT.putStringTag(magazineItem, "casing", casing.toString());
        }
        return magazineItem;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return getDefaultInstance(".50-70:gunsmoke.pointed");
    }

    public static ItemStack getDefaultInstance(String location) {
        return getDefaultInstance(new ResourceLocation(location));
    }

    public static ItemStack getDefaultInstance(ResourceLocation location) {
        return addMaterials(
                getFromMagazine(location),
                "forge:lead",
                "forge:brass"
        );
    }
}
