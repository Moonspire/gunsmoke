package net.ironhorsedevgroup.mods.gunsmoke.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.parts.PartModelOverride;
import net.ironhorsedevgroup.mods.gunsmoke.item.parts.PartUtils;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.content_packs.data.DataFileHandler;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.ItemModelOverrides;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.SimpleItemModelOverride;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.util.ArrayList;
import java.util.List;

public class GunsmokeDataHandler implements DataFileHandler {
    private final List<ResourceLocation> materials = new ArrayList<>();
    private final List<ResourceLocation> parts = new ArrayList<>();
    private final List<ResourceLocation> rounds = new ArrayList<>();
    private final List<ResourceLocation> guns = new ArrayList<>();

    public GunsmokeDataHandler() {}

    @Override
    public void fromJson(JsonObject json) {
        if (json.has("materials")) {
            for (JsonElement entry : json.getAsJsonArray("materials")) {
                materials.add(new ResourceLocation(entry.getAsString()));
            }
        }
        if (json.has("rounds")) {
            for (JsonElement entry : json.getAsJsonArray("rounds")) {
                rounds.add(new ResourceLocation(entry.getAsString()));
            }
        }
        if (json.has("parts")) {
            for (JsonElement entry : json.getAsJsonArray("parts")) {
                parts.add(new ResourceLocation(entry.getAsString()));
            }
        }
        if (json.has("guns")) {
            for (JsonElement entry : json.getAsJsonArray("guns")) {
                guns.add(new ResourceLocation(entry.getAsString()));
            }
        }
    }

    @Override
    public void serverSetupEvent(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();

        ItemModelOverrides.registerItem(GunsmokeItems.PART_ITEM.get(), new PartModelOverride());

        MaterialUtils.loadMaterials(materials, server);
        PartUtils.loadParts(parts, server);
    }
}
