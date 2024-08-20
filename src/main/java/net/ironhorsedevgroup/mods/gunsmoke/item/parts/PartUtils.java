package net.ironhorsedevgroup.mods.gunsmoke.item.parts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.HashMap;
import java.util.Map;

public class PartUtils {
    private static final Map<ResourceLocation, Part> parts = new HashMap<>();

    public static void loadParts(String namespace, JsonArray paths, ResourceManager manager) {
        for (JsonElement entry : paths) {
            String path = entry.getAsString();
            String[] strippedPath = path.split("/");
            ResourceLocation completeLocation = new ResourceLocation(namespace, path);
            ResourceLocation location = new ResourceLocation(namespace, strippedPath[strippedPath.length - 1]);
            Part part = Part.fromJson(Data.readJson(completeLocation, manager));
            Gunsmoke.LOGGER.info("Registering part from {}.json as {}", completeLocation, location);
            parts.put(location, part);
        }
    }

    public static Part getPart(ResourceLocation location) {
        if (parts.containsKey(location)) {
            return parts.get(location);
        }
        return null;
    }

    public static class Part {
        private final Render render;

        private Part(Render render) {
            this.render = render;
        }

        public static Part fromJson(JsonObject json) {
            Render render = Render.fromJson(json.getAsJsonObject("render"));

            return new Part(render);
        }

        public Render getRender() {
            return render;
        }

        public static class Render {
            private final ResourceLocation material;
            private final ResourceLocation accessories;

            private Render(ResourceLocation material, ResourceLocation accessories) {
                this.material = material;
                this.accessories = accessories;
            }

            public static Render fromJson(JsonObject json) {
                ResourceLocation material = new ResourceLocation(json.get("material").getAsString());
                ResourceLocation accessories = null;

                if (json.has("accessories")) {
                    accessories = new ResourceLocation(json.get("accessories").getAsString());
                }

                return new Render(material, accessories);
            }

            public ResourceLocation getMaterial() {
                return material;
            }

            public ResourceLocation getAccessories() {
                return accessories;
            }
        }
    }
}
