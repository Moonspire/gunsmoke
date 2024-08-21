package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrcrayfish.guns.common.GripType;
import com.mrcrayfish.guns.common.Gun;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundUtils;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class GunUtils {
    private static final Map<ResourceLocation, Gun> guns = new HashMap<>();

    public static void loadGuns(String namespace, JsonArray paths, ResourceManager manager) {
        for (JsonElement entry : paths) {
            String path = entry.getAsString();
            String[] strippedPath = path.split("/");
            ResourceLocation completeLocation = new ResourceLocation(namespace, path);
            ResourceLocation location = new ResourceLocation(namespace, strippedPath[strippedPath.length - 1]);
            Gun gun = Gun.fromJson(Data.readJson(completeLocation, manager));
            Gunsmoke.LOGGER.info("Registering gun from {}.json as {}", completeLocation, location);
            updateGun(location, gun);
        }
    }

    public static void updateGun(ResourceLocation location, Gun gun) {
        guns.remove(location);
        guns.put(location, gun);
    }

    public static Gun getGun(ResourceLocation location) {
        if (guns.containsKey(location)) {
            return guns.get(location);
        }
        return new Gun();
    }

    public static Map<ResourceLocation, Gun> getAllGuns() {
        return guns;
    }

    public static class Gun {
        private final Properties properties;
        private final Composition composition;
        private final Magazine magazine;
        private final Sounds sounds;
        private final Render render;

        public Gun() {
            properties = new Properties();
            composition = new Composition();
            magazine = new Magazine();
            sounds = new Sounds();
            render = new Render();
        }

        private Gun(Properties properties, Composition composition, Magazine magazine, Sounds sounds, Render render) {
            this.properties = properties;
            this.composition = composition;
            this.magazine = magazine;
            this.sounds = sounds;
            this.render = render;
        }

        public static Gun fromJson(JsonObject json) {
            Properties properties;
            Composition composition;
            Magazine magazine;
            Sounds sounds;
            Render render;

            if (json.has("properties")) {
                properties = Properties.fromJson(json.getAsJsonObject("properties"));
            } else {
                properties = new Properties();
            }
            if (json.has("composition")) {
                composition = Composition.fromJson(json.getAsJsonObject("composition"));
            } else {
                composition = new Composition();
            }
            if (json.has("magazine")) {
                magazine = Magazine.fromJson(json.getAsJsonObject("magazine"));
            } else {
                magazine = new Magazine();
            }
            if (json.has("sounds")) {
                sounds = Sounds.fromJson(json.getAsJsonObject("sounds"));
            } else {
                sounds = new Sounds();
            }
            if (json.has("render")) {
                render = Render.fromJson(json.getAsJsonObject("render"));
            } else {
                render = new Render();
            }

            return new Gun(properties, composition, magazine, sounds, render);
        }

        public Properties getProperties() {
            return properties;
        }

        public Composition getComposition() {
            return composition;
        }

        public Magazine getMagazine() {
            return magazine;
        }

        public Sounds getSounds() {
            return sounds;
        }

        public Render getRender() {
            return render;
        }

        public static com.mrcrayfish.guns.common.Gun asGun(ItemStack itemStack) {
            com.mrcrayfish.guns.common.Gun.Builder builder = com.mrcrayfish.guns.common.Gun.Builder.create();

            if (itemStack.getItem() instanceof GunItem) {
                Gun gun = getGun(new ResourceLocation(NBT.getStringTag(itemStack, "gun")));

                builder
                        // Display
                        .setMuzzleFlash(
                                gun.getProperties().getFire().getFlashSize(),
                                gun.getProperties().getGeneral().getBarrelEnd().get(0),
                                gun.getProperties().getGeneral().getBarrelEnd().get(1),
                                gun.getProperties().getGeneral().getBarrelEnd().get(2)
                        )

                        // General
                        .setAlwaysSpread(true)
                        .setGripType(GripType.getType(gun.getProperties().getGeneral().getGrip()))
                        .setMaxAmmo(gun.getMagazine().getCapacity())
                        .setProjectileAmount(1)
                        .setFireRate(gun.getProperties().getFire().getCooldown())
                        .setRecoilAdsReduction(gun.getProperties().getFire().getRecoil().getAdsReduction())
                        .setRecoilAngle(gun.getProperties().getFire().getRecoil().getAngle())
                        .setRecoilKick(gun.getProperties().getFire().getRecoil().getKick())
                        .setSpread(gun.getProperties().getFire().getSpread())

                        //Modules
                        .setBarrel(
                                gun.getProperties().getAttachments().getBarrel().getScale().get(0),
                                gun.getProperties().getAttachments().getBarrel().getTranslation().get(0),
                                gun.getProperties().getAttachments().getBarrel().getTranslation().get(1),
                                gun.getProperties().getAttachments().getBarrel().getTranslation().get(2)
                        )
                        .setScope(
                                gun.getProperties().getAttachments().getScope().getScale().get(0),
                                gun.getProperties().getAttachments().getScope().getTranslation().get(0),
                                gun.getProperties().getAttachments().getScope().getTranslation().get(1),
                                gun.getProperties().getAttachments().getScope().getTranslation().get(2)
                        )
                        .setStock(
                                gun.getProperties().getAttachments().getStock().getScale().get(0),
                                gun.getProperties().getAttachments().getStock().getTranslation().get(0),
                                gun.getProperties().getAttachments().getStock().getTranslation().get(1),
                                gun.getProperties().getAttachments().getStock().getTranslation().get(2)
                        )
                        .setUnderBarrel(
                                gun.getProperties().getAttachments().getUnderBarrel().getScale().get(0),
                                gun.getProperties().getAttachments().getUnderBarrel().getTranslation().get(0),
                                gun.getProperties().getAttachments().getUnderBarrel().getTranslation().get(1),
                                gun.getProperties().getAttachments().getUnderBarrel().getTranslation().get(2)
                        )
                        .setZoom(
                                gun.getProperties().getGeneral().getZoom().get(0),
                                gun.getProperties().getGeneral().getZoom().get(1),
                                gun.getProperties().getGeneral().getZoom().get(2),
                                gun.getProperties().getGeneral().getZoom().get(3)
                        )

                        //Projectile
                        .setDamage(1)
                        .setProjectileAffectedByGravity(true)
                        .setAmmo(GunsmokeItems.ROUND_ITEM.get())
                        .setProjectileLife(5)
                        .setProjectileSpeed(10)
                        .setProjectileSize(0.2f)

                        //Sounds
                        .setCockSound(ForgeRegistries.SOUND_EVENTS.getValue(gun.getSounds().getCock()))
                        .setEnchantedFireSound(ForgeRegistries.SOUND_EVENTS.getValue(gun.getSounds().getEnchantedFire()))
                        .setFireSound(ForgeRegistries.SOUND_EVENTS.getValue(gun.getSounds().getFire()))
                        .setSilencedFireSound(ForgeRegistries.SOUND_EVENTS.getValue(gun.getSounds().getSilencedFire()))
                        .setReloadSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cgm:item.pistol.reload")));
            }
            return builder.build();
        }

        public static class Properties {
            private final General general;
            private final Fire fire;
            private final Attachments attachments;

            public Properties() {
                general = new General();
                fire = new Fire();
                attachments = new Attachments();
            }

            private Properties(General general, Fire fire, Attachments attachments) {
                this.general = general;
                this.fire = fire;
                this.attachments = attachments;
            }

            public static Properties fromJson(JsonObject json) {
                General general;
                Fire fire;
                Attachments attachments;

                if (json.has("general")) {
                    general = General.fromJson(json.getAsJsonObject("general"));
                } else {
                    general = new General();
                }
                if (json.has("fire")) {
                    fire = Fire.fromJson(json.getAsJsonObject("fire"));
                } else {
                    fire = new Fire();
                }
                if (json.has("attachments")) {
                    attachments = Attachments.fromJson(json.getAsJsonObject("attachments"));
                } else {
                    attachments = new Attachments();
                }

                return new Properties(general, fire, attachments);
            }

            public General getGeneral() {
                return general;
            }

            public Fire getFire() {
                return fire;
            }

            public Attachments getAttachments() {
                return attachments;
            }

            public static class General {
                private final List<Float> zoom;
                private final List<Float> barrelEnd;
                private final ResourceLocation grip;

                public General() {
                    zoom = List.of(0.6f, 0.0f, 4.6223f, 6.0f);
                    barrelEnd = List.of(0.0f, 0.5f, -8.25f);
                    grip = new ResourceLocation("cgm:two_handed");
                }

                private General(List<Float> zoom, List<Float> barrelEnd, ResourceLocation grip) {
                    this.zoom = zoom;
                    this.barrelEnd = barrelEnd;
                    this.grip = grip;
                }

                public static General fromJson(JsonObject json) {
                    ArrayList<Float> zoom = new ArrayList<>();
                    ArrayList<Float> barrelEnd = new ArrayList<>();
                    ResourceLocation grip = new ResourceLocation("cgm:two_handed");

                    if (json.has("zoom")) {
                        for (JsonElement element : json.getAsJsonArray("zoom")) {
                            zoom.add(element.getAsFloat());
                        }
                    } else {
                        zoom.add(0.6f);
                        zoom.add(0.0f);
                        zoom.add(4.6223f);
                        zoom.add(6.0f);
                    }
                    if (json.has("barrel_end")) {
                        for (JsonElement element : json.getAsJsonArray("barrel_end")) {
                            barrelEnd.add(element.getAsFloat());
                        }
                    } else {
                        barrelEnd.add(0.0f);
                        barrelEnd.add(0.5f);
                        barrelEnd.add(-8.25f);
                    }
                    if (json.has("grip_type")) {
                        grip = new ResourceLocation(json.get("grip_type").getAsString());
                    }

                    return new General(zoom, barrelEnd, grip);
                }

                public List<Float> getZoom() {
                    return zoom;
                }

                public List<Float> getBarrelEnd() {
                    return barrelEnd;
                }

                public ResourceLocation getGrip() {
                    return grip;
                }
            }

            public static class Fire {
                private final byte jamChance;
                private final int cooldown;
                private final float spread;
                private final float flashSize;
                private final Recoil recoil;

                public Fire() {
                    jamChance = 0;
                    cooldown = 10;
                    spread = 0.5f;
                    flashSize = 1.0f;
                    recoil = new Recoil();
                }

                private Fire(byte jamChance, int cooldown, float spread, float flashSize, Recoil recoil) {
                    this.jamChance = jamChance;
                    this.cooldown = cooldown;
                    this.spread = spread;
                    this.flashSize = flashSize;
                    this.recoil = recoil;
                }

                public static Fire fromJson(JsonObject json) {
                    byte jamChance = 0;
                    int cooldown = 10;
                    float spread = 0.4f;
                    float flashSize = 1.0f;
                    Recoil recoil;

                    if (json.has("jam_chance")) {
                        jamChance = json.get("jam_chance").getAsByte();
                    }
                    if (json.has("cooldown")) {
                        cooldown = json.get("cooldown").getAsInt();
                    }
                    if (json.has("spread")) {
                        spread = json.get("spread").getAsFloat();
                    }
                    if (json.has("muzzle_flash_size")) {
                        flashSize = json.get("muzzle_flash_size").getAsFloat();
                    }
                    if (json.has("recoil")) {
                        recoil = Recoil.fromJson(json.getAsJsonObject("recoil"));
                    } else {
                        recoil = new Recoil();
                    }

                    return new Fire(jamChance, cooldown, spread, flashSize, recoil);
                }

                public byte getJamChance() {
                    return jamChance;
                }

                public int getCooldown() {
                    return cooldown;
                }

                public float getSpread() {
                    return spread;
                }

                public float getFlashSize() {
                    return flashSize;
                }

                public Recoil getRecoil() {
                    return recoil;
                }

                public static class Recoil {
                    private final float angle;
                    private final int kick;
                    private final float adsReduction;

                    public Recoil() {
                        this.angle = 10.0f;
                        this.kick = 1;
                        this.adsReduction = 0.5f;
                    }

                    private Recoil(float angle, int kick, float adsReduction) {
                        this.angle = angle;
                        this.kick = kick;
                        this.adsReduction = adsReduction;
                    }

                    public static Recoil fromJson(JsonObject json) {
                        float angle = 10.0f;
                        int kick = 1;
                        float adsReduction = 0.5f;

                        if (json.has("angle")) {
                            angle = json.get("angle").getAsFloat();
                        }
                        if (json.has("kick")) {
                            kick = json.get("kick").getAsInt();
                        }
                        if (json.has("ads_reduction")) {
                            adsReduction = json.get("ads_reduction").getAsFloat();
                        }

                        return new Recoil(angle, kick, adsReduction);
                    }

                    public float getAngle() {
                        return angle;
                    }

                    public int getKick() {
                        return kick;
                    }

                    public float getAdsReduction() {
                        return adsReduction;
                    }
                }
            }
        }

        public static class Composition {
            private final ResourceLocation barrel;
            private final ResourceLocation breach;
            private final ResourceLocation core;
            private final ResourceLocation stock;

            public Composition() {
                barrel = new ResourceLocation("gunsmoke:barrel_medium");
                breach = new ResourceLocation("gunsmoke:chamber_parts");
                core = new ResourceLocation("gunsmoke:gun_parts");
                stock = new ResourceLocation("gunsmoke:simple_stock");
            }

            private Composition(ResourceLocation barrel, ResourceLocation breach, ResourceLocation core, ResourceLocation stock) {
                this.barrel = barrel;
                this.breach = breach;
                this.core = core;
                this.stock = stock;
            }

            public static Composition fromJson(JsonObject json) {
                ResourceLocation barrel;
                ResourceLocation breach;
                ResourceLocation core;
                ResourceLocation stock;

                if (json.has("barrel")) {
                    barrel = new ResourceLocation(json.get("barrel").getAsString());
                } else {
                    barrel = new ResourceLocation("gunsmoke:barrel_medium");
                }
                if (json.has("breach")) {
                    breach = new ResourceLocation(json.get("breach").getAsString());
                } else {
                    breach = new ResourceLocation("gunsmoke:chamber_parts");
                }
                if (json.has("core")) {
                    core = new ResourceLocation(json.get("core").getAsString());
                } else {
                    core = new ResourceLocation("gunsmoke:gun_parts");
                }
                if (json.has("stock")) {
                    stock = new ResourceLocation(json.get("stock").getAsString());
                } else {
                    stock = new ResourceLocation("gunsmoke:simple_stock");
                }

                return new Composition(barrel, breach, core, stock);
            }

            public ResourceLocation getBarrel() {
                return barrel;
            }

            public ResourceLocation getBreach() {
                return breach;
            }

            public ResourceLocation getCore() {
                return core;
            }

            public ResourceLocation getStock() {
                return stock;
            }
        }

        public static class Magazine {
            private final int capacity;
            private final List<Round> rounds;

            public Magazine() {
                capacity = 1;
                rounds = new ArrayList<>();
                rounds.add(new Caliber());
            }

            private Magazine(int capacity, List<Round> rounds) {
                this.capacity = capacity;
                this.rounds = rounds;
            }

            public static Magazine fromJson(JsonObject json) {
                int capacity = 1;
                List<Round> rounds = new ArrayList<>();

                if (json.has("capacity")) {
                    capacity = json.get("capacity").getAsInt();
                }
                if (json.has("rounds")) {
                    for (JsonElement element : json.getAsJsonArray("rounds")) {
                        rounds.add(Round.fromJson(element.getAsJsonObject()));
                    }
                }
                if (rounds.isEmpty()) {
                    rounds.add(new Caliber());
                }

                return new Magazine(capacity, rounds);
            }

            public int getCapacity() {
                return capacity;
            }

            public List<Round> getRounds() {
                return rounds;
            }

            public interface Round {
                static Round fromJson(JsonObject json) {
                    String type = "caliber";
                    if (json.has("type")) {
                        type = json.get("type").getAsString();
                    }
                    if (Objects.equals(type, "caliber")) {
                        return Caliber.fromJson(json);
                    }
                    return null;
                }

                boolean overwritesCapacity();
                int getReloadAmount();
                int getReloadTime();
                ResourceLocation getReloadSound();
            }

            public static class Caliber implements Round {
                private final String caliber;
                private final int reloadAmount;
                private final int reloadTime;
                private final ResourceLocation reloadSound;

                public Caliber() {
                    caliber = "arrow";
                    reloadAmount = 1;
                    reloadTime = 20;
                    reloadSound = new ResourceLocation("cgm:item.pistol.reload");
                }

                private Caliber(String caliber, int reloadAmount, int reloadTime, ResourceLocation reloadSound) {
                    this.caliber = caliber;
                    this.reloadAmount = reloadAmount;
                    this.reloadTime = reloadTime;
                    this.reloadSound = reloadSound;
                }

                public static Caliber fromJson(JsonObject json) {
                    String caliber = json.get("caliber").getAsString();
                    int reloadAmount = 1;
                    int reloadTime = 10;
                    ResourceLocation reloadSound;

                    if (json.has("reload_quantity")) {
                        reloadAmount = json.get("reload_quantity").getAsInt();
                    }
                    if (json.has("reload_time")) {
                        reloadTime = json.get("reload_time").getAsInt();
                    }
                    if (json.has("reload_sound")) {
                        reloadSound = new ResourceLocation(json.get("reload_sound").getAsString());
                    } else {
                        reloadSound = new ResourceLocation("cgm:item.pistol.reload");
                    }

                    return new Caliber(caliber, reloadAmount, reloadTime, reloadSound);
                }

                @Override
                public boolean overwritesCapacity() {
                    return false;
                }

                public String getCaliber() {
                    return caliber;
                }

                @Override
                public int getReloadAmount() {
                    return reloadAmount;
                }

                @Override
                public int getReloadTime() {
                    return reloadTime;
                }

                @Override
                public ResourceLocation getReloadSound() {
                    return reloadSound;
                }
            }
        }

        public static class Sounds {
            private final ResourceLocation cock;
            private final ResourceLocation enchantedFire;
            private final ResourceLocation fire;
            private final ResourceLocation silencedFire;

            public Sounds () {
                cock = new ResourceLocation("cgm:item.rifle.cock");
                enchantedFire = new ResourceLocation("cgm:item.heavy_rifle.enchanted_fire");
                fire = new ResourceLocation("cgm:item.rifle.fire");
                silencedFire = new ResourceLocation("cgm:item.rifle.silenced_fire");
            }

            private Sounds(ResourceLocation cock, ResourceLocation enchantedFire, ResourceLocation fire, ResourceLocation silencedFire) {
                this.cock = cock;
                this.enchantedFire = enchantedFire;
                this.fire = fire;
                this.silencedFire = silencedFire;
            }

            public static Sounds fromJson(JsonObject json) {
                ResourceLocation cock;
                ResourceLocation enchantedFire;
                ResourceLocation fire;
                ResourceLocation silencedFire;
                if (json.has("cock")) {
                    cock = new ResourceLocation(json.get("cock").getAsString());
                } else {
                    cock = new ResourceLocation("cgm:item.rifle.cock");
                }
                if (json.has("enchanted_fire")) {
                    enchantedFire = new ResourceLocation(json.get("enchanted_fire").getAsString());
                } else {
                    enchantedFire = new ResourceLocation("cgm:item.heavy_rifle.enchanted_fire");
                }
                if (json.has("fire")) {
                    fire = new ResourceLocation(json.get("fire").getAsString());
                } else {
                    fire = new ResourceLocation("cgm:item.rifle.fire");
                }
                if (json.has("silenced_fire")) {
                    silencedFire = new ResourceLocation(json.get("silenced_fire").getAsString());
                } else {
                    silencedFire = new ResourceLocation("cgm:item.rifle.silenced_fire");
                }

                return new Sounds(cock, enchantedFire, fire, silencedFire);
            }

            public ResourceLocation getCock() {
                return cock;
            }

            public ResourceLocation getEnchantedFire() {
                return enchantedFire;
            }

            public ResourceLocation getFire() {
                return fire;
            }

            public ResourceLocation getSilencedFire() {
                return silencedFire;
            }
        }

        public static class Render {
            private final ResourceLocation model;
            private final ToolTip toolTip;
            private final Attachments attachments;
            private final Attachments.Attachment muzzleFlash;
            private final IronSights ironSights;

            public Render() {
                model = new ResourceLocation("gunsmoke:models/item/missing_asset.json");
                toolTip = new ToolTip();
                attachments = new Attachments();
                muzzleFlash = new Attachments.Attachment();
                ironSights = new IronSights();
            }

            private Render(ResourceLocation model, ToolTip toolTip, Attachments attachments, Attachments.Attachment muzzleFlash, IronSights ironSights) {
                this.model = model;
                this.toolTip = toolTip;
                this.attachments = attachments;
                this.muzzleFlash = muzzleFlash;
                this.ironSights = ironSights;
            }

            public static Render fromJson(JsonObject json) {
                ResourceLocation model;
                ToolTip toolTip;
                Attachments attachments;
                Attachments.Attachment muzzleFlash;
                IronSights ironSights;

                if (json.has("model")) {
                    model = new ResourceLocation(json.get("model").getAsString());
                } else {
                    model = new ResourceLocation("gunsmoke:models/item/missing_asset.json");
                }
                if (json.has("tool_tip")) {
                    toolTip = ToolTip.fromJson(json.getAsJsonObject("tool_tip"));
                } else {
                    toolTip = new ToolTip();
                }
                if (json.has("attachments")) {
                    attachments = Attachments.fromJson(json.getAsJsonObject("attachments"));
                } else {
                    attachments = new Attachments();
                }
                if (json.has("muzzle_flash")) {
                    muzzleFlash = Attachments.Attachment.fromJson(json.getAsJsonObject("muzzle_flash"));
                } else {
                    muzzleFlash = new Attachments.Attachment();
                }
                if (json.has("iron_sight")) {
                    ironSights = IronSights.fromJson(json.getAsJsonObject("iron_sight"));
                } else {
                    ironSights = new IronSights();
                }

                return new Render(model, toolTip, attachments, muzzleFlash, ironSights);
            }

            public ResourceLocation getModel() {
                return model;
            }

            public ToolTip getToolTip() {
                return toolTip;
            }

            public Attachments getAttachments() {
                return attachments;
            }

            public Attachments.Attachment getMuzzleFlash() {
                return muzzleFlash;
            }

            public IronSights getIronSights() {
                return ironSights;
            }

            public static class ToolTip {
                private final String barrel;
                private final String breach;
                private final String core;
                private final String stock;

                public ToolTip() {
                    barrel = "tooltip.gunsmoke.barrel_material";
                    breach = "tooltip.gunsmoke.breach_material";
                    core = "tooltip.gunsmoke.core_material";
                    stock = "tooltip.gunsmoke.stock_material";
                }

                private ToolTip(String barrel, String breach, String core, String stock) {
                    this.barrel = barrel;
                    this.breach = breach;
                    this.core = core;
                    this.stock = stock;
                }

                public static ToolTip fromJson(JsonObject json) {
                    String barrel;
                    String breach;
                    String core;
                    String stock;

                    if (json.has("barrel")) {
                        barrel = json.get("barrel").getAsString();
                    } else {
                        barrel = "tooltip.gunsmoke.barrel_material";
                    }
                    if (json.has("breach")) {
                        breach = json.get("breach").getAsString();
                    } else {
                        breach = "tooltip.gunsmoke.breach_material";
                    }
                    if (json.has("core")) {
                        core = json.get("core").getAsString();
                    } else {
                        core = "tooltip.gunsmoke.core_material";
                    }
                    if (json.has("stock")) {
                        stock = json.get("stock").getAsString();
                    } else {
                        stock = "tooltip.gunsmoke.stock_material";
                    }

                    return new ToolTip(barrel, breach, core, stock);
                }

                public String getBarrel() {
                    return barrel;
                }

                public String getBreach() {
                    return breach;
                }

                public String getCore() {
                    return core;
                }

                public String getStock() {
                    return stock;
                }
            }

            public static class IronSights {
                private final List<Float> camera;
                private final float fov;

                public IronSights() {
                    camera = List.of(8.0f, 1.5f, 18.0f);
                    fov = 60.0f;
                }

                private IronSights(List<Float> camera, float fov) {
                    this.camera = camera;
                    this.fov = fov;
                }

                public static IronSights fromJson(JsonObject json) {
                    List<Float> camera = new ArrayList<>();
                    float fov = 60.0f;

                    if (json.has("camera")) {
                        camera = new ArrayList<>();
                        for (JsonElement element : json.getAsJsonArray("camera")) {
                            camera.add(element.getAsFloat());
                        }
                    }
                    if (camera.isEmpty()) {
                        camera.add(8.0f);
                        camera.add(1.5f);
                        camera.add(18.0f);
                    }
                    if (json.has("fov")) {
                        fov = json.get("fov").getAsFloat();
                    }

                    return new IronSights(camera, fov);
                }

                public List<Float> getCamera() {
                    return camera;
                }

                public float getFov() {
                    return fov;
                }
            }
        }

        public static class Attachments {
            private final Attachment barrel;
            private final Attachment scope;
            private final Attachment stock;
            private final Attachment underBarrel;

            public Attachments() {
                barrel = new Attachment();
                scope = new Attachment();
                stock = new Attachment();
                underBarrel = new Attachment();
            }

            private Attachments(Attachment barrel, Attachment scope, Attachment stock, Attachment underBarrel) {
                this.barrel = barrel;
                this.scope = scope;
                this.stock = stock;
                this.underBarrel = underBarrel;
            }

            public static Attachments fromJson(JsonObject json) {
                Attachment barrel;
                Attachment scope;
                Attachment stock;
                Attachment underBarrel;

                if (json.has("barrel")) {
                    barrel = Attachment.fromJson(json.getAsJsonObject("barrel"));
                } else {
                    barrel = new Attachment();
                }
                if (json.has("scope")) {
                    scope = Attachment.fromJson(json.getAsJsonObject("scope"));
                } else {
                    scope = new Attachment();
                }
                if (json.has("stock")) {
                    stock = Attachment.fromJson(json.getAsJsonObject("stock"));
                } else {
                    stock = new Attachment();
                }
                if (json.has("under_barrel")) {
                    underBarrel = Attachment.fromJson(json.getAsJsonObject("under_barrel"));
                } else {
                    underBarrel = new Attachment();
                }

                return new Attachments(barrel, scope, stock, underBarrel);
            }

            public Attachment getBarrel() {
                return barrel;
            }

            public Attachment getScope() {
                return scope;
            }

            public Attachment getStock() {
                return stock;
            }

            public Attachment getUnderBarrel() {
                return underBarrel;
            }

            public static class Attachment {
                private final List<Float> translation;
                private final List<Float> scale;

                public Attachment() {
                    translation = new ArrayList<>();
                    scale = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        translation.add(0.0f);
                        scale.add(1.0f);
                    }
                }

                private Attachment(List<Float> translation, List<Float> scale) {
                    this.translation = translation;
                    this.scale = scale;
                }

                public static Attachment fromJson(JsonObject json) {
                    List<Float> translation = new ArrayList<>();
                    List<Float> scale = new ArrayList<>();

                    if (json.has("translation")) {
                        for (JsonElement element : json.getAsJsonArray("translation")) {
                            translation.add(element.getAsFloat());
                        }
                    } else {
                        translation.add(0.0f);
                        translation.add(0.0f);
                        translation.add(0.0f);
                    }
                    if (json.has("scale")) {
                        for (JsonElement element : json.getAsJsonArray("scale")) {
                            scale.add(element.getAsFloat());
                        }
                    } else {
                        scale.add(1.0f);
                        scale.add(1.0f);
                        scale.add(1.0f);
                    }

                    return new Attachment(translation, scale);
                }

                public List<Float> getTranslation() {
                    return translation;
                }

                public List<Float> getScale() {
                    return scale;
                }
            }
        }
    }
}
