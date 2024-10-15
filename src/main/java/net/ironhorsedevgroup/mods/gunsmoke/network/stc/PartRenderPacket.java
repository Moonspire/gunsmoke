package net.ironhorsedevgroup.mods.gunsmoke.network.stc;

import net.ironhorsedevgroup.mods.gunsmoke.item.parts.Parts;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PartRenderPacket {
    public ResourceLocation location;
    public ResourceLocation model;
    public List<ResourceLocation> materials;

    public PartRenderPacket(ResourceLocation location, Parts.Part part) {
        this.location = location;
        model = part.getRender().getModel();
        materials = part.getRender().getMaterials();
    }

    public PartRenderPacket(ResourceLocation location, ResourceLocation model, List<ResourceLocation> materials) {
        this.location = location;
        this.model = model;
        this.materials = materials;
    }

    public static PartRenderPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        ResourceLocation model = buf.readResourceLocation();
        int len = buf.readInt();
        List<ResourceLocation> materials = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            materials.add(buf.readResourceLocation());
        }
        return new PartRenderPacket(location, model, materials);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeResourceLocation(model);
        buf.writeInt(materials.size());
        for (ResourceLocation material : materials) {
            buf.writeResourceLocation(material);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Parts.loadPart(this);
        });
        return true;
    }
}
