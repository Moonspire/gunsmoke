package net.ironhorsedevgroup.mods.gunsmoke.item.parts;

import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.network.GunsmokeMessages;
import net.ironhorsedevgroup.mods.gunsmoke.network.packets.stc.PartRenderPacket;
import net.ironhorsedevgroup.mods.toolshed.content_packs.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartUtils {
    private static final Map<ResourceLocation, Part> parts = new HashMap<>();

    public static void loadParts(List<ResourceLocation> parts, MinecraftServer server) {
        for (ResourceLocation part : parts) {
            loadPart(part, server);
        }
    }

    public static void loadPart(ResourceLocation location, MinecraftServer server) {
        Part part = Part.fromJson(DataLoader.loadJson(location, server));
        String[] strippedPath = location.getPath().split("/");
        location = new ResourceLocation(location.getNamespace(), strippedPath[strippedPath.length - 1]);
        Gunsmoke.LOGGER.info("Registering server part: {}", location);
        updatePart(location, part);
    }

    public static void loadPart(PartRenderPacket packet) {
        ResourceLocation location = packet.location;
        Gunsmoke.LOGGER.info("Registering client part: {}", location);
        updatePart(location, Part.fromPacket(packet));
    }

    public static void updatePart(ResourceLocation location, Part part) {
        parts.remove(location);
        parts.put(location, part);
    }

    public static Part getPart(ResourceLocation location) {
        if (parts.containsKey(location)) {
            return parts.get(location);
        }
        return new Part();
    }

    public static Part getPart(ItemStack stack) {
        return getPart(NBT.getLocationTag(stack, "part"));
    }

    public static void sendParts(ServerPlayer player) {
        for (ResourceLocation part : parts.keySet()) {
            GunsmokeMessages.sendToPlayer(new PartRenderPacket(part, parts.get(part).render.getModel()), player);
        }
    }

    public static Map<ResourceLocation, Part> getAllParts() {
        return parts;
    }

    public static class Part {
        private final Render render;

        public Part() {
            render = new Render();
        }

        private Part(Render render) {
            this.render = render;
        }

        public static Part fromJson(JsonObject json) {
            Render render = Render.fromJson(json.getAsJsonObject("render"));

            return new Part(render);
        }

        public static Part fromPacket(PartRenderPacket packet) {
            Render render = Render.fromPacket(packet);

            return new Part(render);
        }

        public Render getRender() {
            return render;
        }

        public static class Render {
            private final ResourceLocation model;

            public Render() {
                model = new ResourceLocation("gunsmoke:parts/parts");
            }

            private Render(ResourceLocation model) {
                this.model = model;
            }

            public static Render fromJson(JsonObject json) {
                ResourceLocation model;

                if (json.has("model")) {
                    model = new ResourceLocation(json.get("model").getAsString());
                } else {
                    model = new ResourceLocation("gunsmoke:parts/parts");
                }

                return new Render(model);
            }

            public static Render fromPacket(PartRenderPacket packet) {
                return new Render(packet.model);
            }

            public ResourceLocation getModel() {
                return model;
            }
        }
    }
}