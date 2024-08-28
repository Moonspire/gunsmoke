package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundUtils;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.ItemModelOverrides;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.SimpleItemModelOverride;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class RoundItem extends Item {
    public RoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            Map<String, Map<String, RoundUtils.Round>> allRounds = RoundUtils.getAllRounds();
            for (String caliber : allRounds.keySet()) {
                for (String round : allRounds.get(caliber).keySet()) {
                    itemStack.add(getDefaultInstance(new ResourceLocation(caliber, round)));
                }
            }
        }
    }

    public static ItemStack getFromRound(String location) {
        return getFromRound(new ResourceLocation(location));
    }

    public static ItemStack getFromRound(ResourceLocation location) {
        RoundUtils.Round round = RoundUtils.getRound(location);
        if (round instanceof RoundUtils.ItemRound itemRound) {
            return new ItemStack(itemRound.getItem());
        }
        ItemStack roundItem = new ItemStack(GunsmokeItems.ROUND_ITEM.get());
        NBT.putStringTag(roundItem, "round", location.toString());
        return roundItem;
    }

    public static ItemStack addMaterials(ItemStack roundItem, String round, String casing) {
        return addMaterials(roundItem, new ResourceLocation(round), new ResourceLocation(casing));
    }

    public static ItemStack addMaterials(ItemStack roundItem, ResourceLocation round, ResourceLocation casing) {
        if (roundItem.getItem() instanceof RoundItem) {
            NBT.putStringTag(roundItem, "material_0", round.toString());
            NBT.putStringTag(roundItem, "material_1", casing.toString());
        }
        return roundItem;
    }

    public RoundUtils.Round getRound(ItemStack stack) {
        return RoundUtils.getRound(new ResourceLocation(NBT.getStringTag(stack, "round")));
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
                getFromRound(location),
                "forge:lead",
                "forge:brass"
        );
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        ResourceLocation location = new ResourceLocation(NBT.getStringTag(itemStack, "round"));
        return "round." + location.getNamespace() + "." + location.getPath();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
        if (level != null && level.isClientSide) {
            ResourceLocation round = new ResourceLocation(NBT.getStringTag(stack, "material_0"));
            ResourceLocation casing = new ResourceLocation(NBT.getStringTag(stack, "material_1"));

            components.add(Component.literal(I18n.get("tooltip.gunsmoke.round_material") + ": " + I18n.get(MaterialUtils.getMaterialLang(round))));
            components.add(Component.literal(I18n.get("tooltip.gunsmoke.casing_material") + ": " + I18n.get(MaterialUtils.getMaterialLang(casing))));
        }
    }
}
