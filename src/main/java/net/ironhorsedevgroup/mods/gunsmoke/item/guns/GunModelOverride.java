package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.toolshed.content_packs.ItemModelOverride;
import net.ironhorsedevgroup.mods.toolshed.content_packs.SimpleItemModelOverride;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GunModelOverride implements ItemModelOverride {
    private final Map<ResourceLocation, BakedModel> models;

    public GunModelOverride() {
        this.models = new HashMap<>();
    }

    public void addModel(ResourceLocation location) {
        ResourceLocation model = GunUtils.getGun(location).getRender().getModel();
        models.put(location, SimpleItemModelOverride.fromLocation(model).getModel());
    }

    public BakedModel getModel(ResourceLocation location) {
        if (models.containsKey(location)) {
            return models.get(location);
        }
        return Minecraft.getInstance().getModelManager().getMissingModel();
    }

    @Override
    public BakedModel getModel(ItemStack itemStack) {
        return getModel(NBT.getLocationTag(itemStack, "gun"));
    }

    @Override
    public BakedModel getModel(Item item) {
        return Minecraft.getInstance().getModelManager().getMissingModel();
    }

    @Override
    public BakedModel getModel() {
        return Minecraft.getInstance().getModelManager().getMissingModel();
    }
}
