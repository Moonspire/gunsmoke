package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.CaliberProperties;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RoundItem extends Item {
    private final CaliberProperties caliber;

    public RoundItem(Properties properties, CaliberProperties caliber) {
        super(properties);
        this.caliber = caliber;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (RoundProperties round : this.caliber.getRounds()) {
                ItemStack roundItem = new ItemStack(this);
                NBT.putIntTag(roundItem, "CustomModelData", round.getId());
                NBT.putStringTag(roundItem, "material_0", GunMaterials.LEAD.getSerializedName());
                NBT.putStringTag(roundItem, "material_1", GunMaterials.BRASS.getSerializedName());
                itemStack.add(roundItem);
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return I18n.get(this.getDescriptionId() + "." + NBT.getIntTag(itemStack, "CustomModelData"));
    }

    public RoundProperties getRound(ItemStack itemStack) {
        return caliber.getRound(NBT.getIntTag(itemStack, "CustomModelData"));
    }

    public RoundProperties getModifiedRound(ItemStack itemStack) {
        return getRound(itemStack);
    }

    public CaliberProperties getCaliber() {
        return caliber;
    }
}
