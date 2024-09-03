package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.RoundRenderPacket;

public class DynamicRound implements Round {
    private final String caliber;
    private final Damage damage;
    private final Properties properties;
    private final Render render;

    private DynamicRound(String caliber, Damage damage, Properties properties, Render render) {
        this.caliber = caliber;
        this.damage = damage;
        this.properties = properties;
        this.render = render;
    }

    public static Round fromJson(JsonObject json) {
        String caliber = json.get("caliber").getAsString();
        Damage damage;
        Properties properties;
        Render render;

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
        if (json.has("render")) {
            render = Render.fromJson(json.getAsJsonObject("render"));
        } else {
            render = new Render();
        }

        return new DynamicRound(caliber, damage, properties, render);
    }

    public static Round fromPacket(RoundRenderPacket packet) {
        String caliber = packet.location.getNamespace();
        Damage damage = new Damage();
        Properties properties = new Properties();
        Render render = Render.fromPacket(packet);

        return new DynamicRound(caliber, damage, properties, render);
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
        return true;
    }

    @Override
    public Render getRender() {
        return render;
    }
}
