package net.ironhorsedevgroup.mods.gunsmoke.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Guns;
import net.ironhorsedevgroup.mods.gunsmoke.item.parts.Parts;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Rounds;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.data.DataFileHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.util.ArrayList;
import java.util.List;

public class GunsmokeDataHandler implements DataFileHandler {
    private List<ResourceLocation> parts = new ArrayList<>();
    private List<ResourceLocation> rounds = new ArrayList<>();
    private List<ResourceLocation> guns = new ArrayList<>();

    public GunsmokeDataHandler() {}

    @Override
    public void fromJson(JsonObject json) {
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

        Parts.loadParts(parts, server);
        Rounds.loadRounds(rounds, server);
        Guns.loadGuns(guns, server);

        parts = null;
        rounds = null;
        guns = null;
    }

    @Override
    public void joinSTC(ServerPlayer player) {
        Parts.sendParts(player);
        Rounds.sendRounds(player);
        Guns.sendGuns(player);
    }
}
