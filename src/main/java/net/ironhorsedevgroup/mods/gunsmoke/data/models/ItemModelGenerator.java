package net.ironhorsedevgroup.mods.gunsmoke.data.models;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.CaliberProperties;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeCalibers;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
    private static final ResourceLocation ITEM_GENERATED = new ResourceLocation("item/generated");

    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (GunsmokeCalibers calibers : GunsmokeCalibers.values()) {
            roundItem(calibers.getCaliber());
        }
    }

    private ItemModelBuilder roundItem(CaliberProperties caliber) {
        ItemModelBuilder builder = withExistingParent(caliber.getName(), ITEM_GENERATED);
        String lastPath = "";
        for (RoundProperties round : caliber.getRounds()) {
            String path = round.getTexture().toString();
            if (!path.equals(lastPath)) {
                lastPath = path;
                if (round.getId() == 0) {
                    if (round.isRoundRendered()) {
                        builder.texture("layer0", path + "/round");
                    } else {
                        builder.texture("layer0", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (!round.isCaseless()) {
                        builder.texture("layer1", path + "/casing");
                    } else {
                        builder.texture("layer1", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (round.hasColor()) {
                        builder.texture("layer2", path + "/color");
                    } else if (round.hasAccessories()) {
                        builder.texture("layer2", path + "/accessories");
                    }
                } else {
                    ItemModelBuilder predicate = withExistingParent(caliber.getName() + "." + round.getId(), ITEM_GENERATED);
                    if (round.isRoundRendered()) {
                        predicate.texture("layer0", path + "/round");
                    } else {
                        predicate.texture("layer0", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (!round.isCaseless()) {
                        predicate.texture("layer1", path + "/casing");
                    } else {
                        predicate.texture("layer1", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (round.hasColor()) {
                        predicate.texture("layer2", path + "/color");
                    } else if (round.hasAccessories()) {
                        predicate.texture("layer2", path + "/accessories");
                    }

                    builder.override()
                            .predicate(new ResourceLocation("custom_model_data"), round.getId())
                            .model(predicate);
                }
            }
        }
        return builder;
    }
}
