package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PrintItem extends Item {
    public PrintItem(Properties properties) {
        super(properties);
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tip, flag);
        tip.add(
                Component.translatable(
                        Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                                        new ResourceLocation(
                                                "gunsmoke",
                                                NBT.getStringTag(
                                                        stack,
                                                        "printType"
                                                )
                                        )
                                ))
                        .getDescriptionId()
                )
        );
    }
}
