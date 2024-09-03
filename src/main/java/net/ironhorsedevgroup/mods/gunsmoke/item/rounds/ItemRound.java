package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;


import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRound implements Round {
    private final String caliber;
    private final ResourceLocation item;
    private final Damage damage;
    private final Properties properties;

    private ItemRound(String caliber, ResourceLocation item, Damage damage, Properties properties) {
        this.caliber = caliber;
        this.item = item;
        this.damage = damage;
        this.properties = properties;
    }

    public static ItemRound fromJson(JsonObject json) {
        String caliber = json.get("caliber").getAsString();
        ResourceLocation item = new ResourceLocation(json.get("item").getAsString());
        Rounds.setItemRound(item, caliber);
        Damage damage;
        Properties properties;

        if (json.has("damage")) {
            damage = Damage.fromJson(json.getAsJsonObject("damage"));
        } else {
            damage = new Damage();
        }
        if (json.has("properties")) {
            properties = Properties.fromJson(json.getAsJsonObject("properties"));
        } else {
            properties = new Properties();
        }

        return new ItemRound(caliber, item, damage, properties);
    }

    public static ItemRound fromPacket(RoundItemPacket packet) {
        String caliber = packet.location.getNamespace();
        ResourceLocation item = packet.item;
        Damage damage = new Damage();
        Properties properties = new Properties();

        return new ItemRound(caliber, item, damage, properties);
    }

    @Override
    public String getCaliber() {
        return caliber;
    }

    @Override
    public Damage getDamage() {
        return damage;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public boolean hasRenderer() {
        return false;
    }

    public ResourceLocation getItemLocation() {
        return item;
    }

    public Item getItem() {
        try {
            return ForgeRegistries.ITEMS.getValue(item);
        } catch (Exception e) {
            return Items.AIR;
        }
    }
}
