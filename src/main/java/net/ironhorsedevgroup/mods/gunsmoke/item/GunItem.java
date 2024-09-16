package net.ironhorsedevgroup.mods.gunsmoke.item;

import com.mrcrayfish.guns.common.Gun;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.DynamicGun;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Guns;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GunItem extends com.mrcrayfish.guns.item.GunItem {
    public GunItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            Map<ResourceLocation, DynamicGun> allGuns = Guns.getAllGuns();
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
        NBT.putLocationTag(stack, "gun", location);
        DynamicGun.Composition composition = Guns.getGun(location).getComposition();
        NBT.putLocationTag(stack, "barrel", composition.getBarrel().getMaterial());
        NBT.putLocationTag(stack, "breach", composition.getBreach().getMaterial());
        NBT.putLocationTag(stack, "core", composition.getCore().getMaterial());
        NBT.putLocationTag(stack, "stock", composition.getStock().getMaterial());
        return stack;
    }

    public static GunItem get() {
        if (GunsmokeItems.GUN_ITEM.get() instanceof GunItem gun) {
            return gun;
        }
        Minecraft.crash(new CrashReport("gunsmoke:gun_item is not of type GunItem, should never happen =/", new Throwable()));
        return new GunItem(new Properties());
    }

    @Override
    public Gun getModifiedGun(ItemStack stack) {
        return Guns.getGun(stack).asGun();
    }
}
