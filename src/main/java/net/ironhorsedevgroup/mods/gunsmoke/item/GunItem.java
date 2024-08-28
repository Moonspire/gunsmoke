package net.ironhorsedevgroup.mods.gunsmoke.item;

import com.mrcrayfish.guns.common.Gun;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunModelOverride;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunUtils;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.ItemModelOverrides;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GunItem extends com.mrcrayfish.guns.item.GunItem {
    public GunItem(Properties properties) {
        super(properties);
        ItemModelOverrides.registerItem(this, new GunModelOverride());
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            Map<ResourceLocation, GunUtils.Gun> allGuns = GunUtils.getAllGuns();
            for (ResourceLocation gun : allGuns.keySet()) {
                itemStack.add(getDefaultInstance(gun));
            }
        }
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        return getDefaultInstance("gunsmoke:sharps");
    }

    public @NotNull ItemStack getDefaultInstance(String location) {
        return getDefaultInstance(new ResourceLocation(location));
    }

    public @NotNull ItemStack getDefaultInstance(ResourceLocation location) {
        ItemStack stack = super.getDefaultInstance();
        NBT.putStringTag(stack, "gun", location.toString());
        GunUtils.Gun.Composition composition = GunUtils.getGun(location).getComposition();
        NBT.putStringTag(stack, "barrel", composition.getBarrel().getMaterial().toString());
        NBT.putStringTag(stack, "breach", composition.getBreach().getMaterial().toString());
        NBT.putStringTag(stack, "core", composition.getCore().getMaterial().toString());
        NBT.putStringTag(stack, "stock", composition.getStock().getMaterial().toString());
        return stack;
    }

    @Override
    public Gun getModifiedGun(ItemStack stack) {
        return GunUtils.Gun.asGun(stack);
    }
}
