package net.ironhorsedevgroup.mods.gunsmoke.item.parts;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.model.ItemModelOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PartModelOverride implements ItemModelOverride {
    public PartModelOverride() {}

    @Override
    public BakedModel getModel(ItemStack itemStack) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        ModelResourceLocation location = new ModelResourceLocation(Parts.getPart(itemStack).getRender().getModel(), "inventory");
        return manager.getModel(location);
    }

    @Override
    public BakedModel getModel(Item item) {
        return getModel();
    }

    @Override
    public BakedModel getModel() {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        ModelResourceLocation location = new ModelResourceLocation("gunsmoke:parts/parts#inventory");
        Gunsmoke.LOGGER.info("Fetching model: {}", location);
        return manager.getModel(location);
    }
}
