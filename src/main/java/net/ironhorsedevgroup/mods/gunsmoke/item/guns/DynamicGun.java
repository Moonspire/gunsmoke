package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.GripType;
import com.mrcrayfish.guns.common.Gun;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.GunMagazine;
import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.Magazine;
import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.MaterialMagazine;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Rounds;
import net.ironhorsedevgroup.mods.gunsmoke.network.stc.GunRenderPacket;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.materials.Material;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DynamicGun implements net.ironhorsedevgroup.mods.gunsmoke.item.guns.Gun {
    private final DynamicGun.Properties properties;
    private final DynamicGun.Composition composition;
    private final DynamicGun.RoundStorage magazine;
    private final DynamicGun.Sounds sounds;
    private final DynamicGun.Render render;

    public DynamicGun() {
        properties = new DynamicGun.Properties();
        composition = new DynamicGun.Composition();
        magazine = new RoundStorage();
        sounds = new DynamicGun.Sounds();
        render = new DynamicGun.Render();
    }

    private DynamicGun(DynamicGun.Properties properties, DynamicGun.Composition composition, RoundStorage magazine, DynamicGun.Sounds sounds, DynamicGun.Render render) {
        this.properties = properties;
        this.composition = composition;
        this.magazine = magazine;
        this.sounds = sounds;
        this.render = render;
    }

    public static DynamicGun fromJson(JsonObject json) {
        DynamicGun.Properties properties;
        DynamicGun.Composition composition;
        RoundStorage magazine;
        DynamicGun.Sounds sounds;
        DynamicGun.Render render;

        if (json.has("properties")) {
            properties = DynamicGun.Properties.fromJson(json.getAsJsonObject("properties"));
        } else {
            properties = new DynamicGun.Properties();
        }
        if (json.has("composition")) {
            composition = DynamicGun.Composition.fromJson(json.getAsJsonObject("composition"));
        } else {
            composition = new DynamicGun.Composition();
        }
        if (json.has("magazine")) {
            magazine = RoundStorage.fromJson(json.getAsJsonObject("magazine"));
        } else {
            magazine = new RoundStorage();
        }
        if (json.has("sounds")) {
            sounds = DynamicGun.Sounds.fromJson(json.getAsJsonObject("sounds"));
        } else {
            sounds = new DynamicGun.Sounds();
        }
        if (json.has("render")) {
            render = DynamicGun.Render.fromJson(json.getAsJsonObject("render"));
        } else {
            render = new DynamicGun.Render();
        }

        return new DynamicGun(properties, composition, magazine, sounds, render);
    }

    public static DynamicGun fromPacket(GunRenderPacket packet) {
        DynamicGun.Properties properties = new DynamicGun.Properties();
        DynamicGun.Composition composition = packet.composition;
        RoundStorage magazine = new RoundStorage();
        DynamicGun.Sounds sounds = new DynamicGun.Sounds();
        DynamicGun.Render render = DynamicGun.Render.fromPacket(packet);
        return new DynamicGun(properties, composition, magazine, sounds, render);
    }

    @Override
    public DynamicGun.Properties getProperties() {
        return properties;
    }

    @Override
    public DynamicGun.Composition getComposition() {
        return composition;
    }

    @Override
    public Magazine getMagazine() {
        return GunMagazine.fromGun(this);
    }

    @Override
    public RoundStorage getRoundStorage() {
        return magazine;
    }

    public DynamicGun.Sounds getSounds() {
        return sounds;
    }

    public DynamicGun.Render getRender() {
        return render;
    }

    @Override
    public Gun asGun() {
        Gun.Builder builder = Gun.Builder.create();

            builder
                // Display
                .setMuzzleFlash(
                        this.getProperties().getFire().getFlashSize(),
                        this.getProperties().getGeneral().getBarrelEnd().get(0),
                        this.getProperties().getGeneral().getBarrelEnd().get(1),
                        this.getProperties().getGeneral().getBarrelEnd().get(2)
                )

                // General
                .setAlwaysSpread(true)
                .setGripType(GripType.getType(this.getProperties().getGeneral().getGrip()))
                .setMaxAmmo(this.getMagazine().getCapacity())
                .setProjectileAmount(1)
                .setFireRate(this.getProperties().getFire().getCooldown())
                .setRecoilAdsReduction(this.getProperties().getFire().getRecoil().getAdsReduction())
                .setRecoilAngle(this.getProperties().getFire().getRecoil().getAngle())
                .setRecoilKick(this.getProperties().getFire().getRecoil().getKick())
                .setSpread(this.getProperties().getFire().getSpread())

                //Modules
                .setBarrel(
                        this.getProperties().getAttachments().getBarrel().getScale().get(0),
                        this.getProperties().getAttachments().getBarrel().getTranslation().get(0),
                        this.getProperties().getAttachments().getBarrel().getTranslation().get(1),
                        this.getProperties().getAttachments().getBarrel().getTranslation().get(2)
                )
                .setScope(
                        this.getProperties().getAttachments().getScope().getScale().get(0),
                        this.getProperties().getAttachments().getScope().getTranslation().get(0),
                        this.getProperties().getAttachments().getScope().getTranslation().get(1),
                        this.getProperties().getAttachments().getScope().getTranslation().get(2)
                )
                .setStock(
                        this.getProperties().getAttachments().getStock().getScale().get(0),
                        this.getProperties().getAttachments().getStock().getTranslation().get(0),
                        this.getProperties().getAttachments().getStock().getTranslation().get(1),
                        this.getProperties().getAttachments().getStock().getTranslation().get(2)
                )
                .setUnderBarrel(
                        this.getProperties().getAttachments().getUnderBarrel().getScale().get(0),
                        this.getProperties().getAttachments().getUnderBarrel().getTranslation().get(0),
                        this.getProperties().getAttachments().getUnderBarrel().getTranslation().get(1),
                        this.getProperties().getAttachments().getUnderBarrel().getTranslation().get(2)
                )
                .setZoom(
                        this.getProperties().getGeneral().getZoom().get(0),
                        this.getProperties().getGeneral().getZoom().get(1),
                        this.getProperties().getGeneral().getZoom().get(2),
                        this.getProperties().getGeneral().getZoom().get(3)
                )

                //Projectile
                .setDamage(1)
                .setProjectileAffectedByGravity(true)
                .setAmmo(GunsmokeItems.ROUND_ITEM.get())
                .setProjectileLife(5)
                .setProjectileSpeed(10)
                .setProjectileSize(0.2f)

                //Sounds
                .setCockSound(ForgeRegistries.SOUND_EVENTS.getValue(this.getSounds().getCock()))
                .setEnchantedFireSound(ForgeRegistries.SOUND_EVENTS.getValue(this.getSounds().getEnchantedFire()))
                .setFireSound(ForgeRegistries.SOUND_EVENTS.getValue(this.getSounds().getFire()))
                .setSilencedFireSound(ForgeRegistries.SOUND_EVENTS.getValue(this.getSounds().getSilencedFire()))
                .setReloadSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cgm:item.pistol.reload")));

        return builder.build();
    }

    public static class Properties {
        private final DynamicGun.Properties.General general;
        private final DynamicGun.Properties.Fire fire;
        private final DynamicGun.Attachments attachments;

        public Properties() {
            general = new DynamicGun.Properties.General();
            fire = new DynamicGun.Properties.Fire();
            attachments = new DynamicGun.Attachments();
        }

        private Properties(DynamicGun.Properties.General general, DynamicGun.Properties.Fire fire, DynamicGun.Attachments attachments) {
            this.general = general;
            this.fire = fire;
            this.attachments = attachments;
        }

        public static DynamicGun.Properties fromJson(JsonObject json) {
            DynamicGun.Properties.General general;
            DynamicGun.Properties.Fire fire;
            DynamicGun.Attachments attachments;

            if (json.has("general")) {
                general = DynamicGun.Properties.General.fromJson(json.getAsJsonObject("general"));
            } else {
                general = new DynamicGun.Properties.General();
            }
            if (json.has("fire")) {
                fire = DynamicGun.Properties.Fire.fromJson(json.getAsJsonObject("fire"));
            } else {
                fire = new DynamicGun.Properties.Fire();
            }
            if (json.has("attachments")) {
                attachments = DynamicGun.Attachments.fromJson(json.getAsJsonObject("attachments"));
            } else {
                attachments = new DynamicGun.Attachments();
            }

            return new DynamicGun.Properties(general, fire, attachments);
        }

        public DynamicGun.Properties.General getGeneral() {
            return general;
        }

        public DynamicGun.Properties.Fire getFire() {
            return fire;
        }

        public DynamicGun.Attachments getAttachments() {
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

            public static DynamicGun.Properties.General fromJson(JsonObject json) {
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

                return new DynamicGun.Properties.General(zoom, barrelEnd, grip);
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
            private final DynamicGun.Properties.Fire.Recoil recoil;

            public Fire() {
                jamChance = 0;
                cooldown = 10;
                spread = 0.5f;
                flashSize = 1.0f;
                recoil = new DynamicGun.Properties.Fire.Recoil();
            }

            private Fire(byte jamChance, int cooldown, float spread, float flashSize, DynamicGun.Properties.Fire.Recoil recoil) {
                this.jamChance = jamChance;
                this.cooldown = cooldown;
                this.spread = spread;
                this.flashSize = flashSize;
                this.recoil = recoil;
            }

            public static DynamicGun.Properties.Fire fromJson(JsonObject json) {
                byte jamChance = 0;
                int cooldown = 10;
                float spread = 0.4f;
                float flashSize = 1.0f;
                DynamicGun.Properties.Fire.Recoil recoil;

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
                    recoil = DynamicGun.Properties.Fire.Recoil.fromJson(json.getAsJsonObject("recoil"));
                } else {
                    recoil = new DynamicGun.Properties.Fire.Recoil();
                }

                return new DynamicGun.Properties.Fire(jamChance, cooldown, spread, flashSize, recoil);
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

            public DynamicGun.Properties.Fire.Recoil getRecoil() {
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

                public static DynamicGun.Properties.Fire.Recoil fromJson(JsonObject json) {
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

                    return new DynamicGun.Properties.Fire.Recoil(angle, kick, adsReduction);
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
        private final DynamicGun.Composition.Part barrel;
        private final DynamicGun.Composition.Part breach;
        private final DynamicGun.Composition.Part core;
        private final DynamicGun.Composition.Part stock;

        public Composition() {
            barrel = new DynamicGun.Composition.Part();
            breach = new DynamicGun.Composition.Part();
            core = new DynamicGun.Composition.Part();
            stock = new DynamicGun.Composition.Part();
        }

        private Composition(DynamicGun.Composition.Part barrel, DynamicGun.Composition.Part breach, DynamicGun.Composition.Part core, DynamicGun.Composition.Part stock) {
            this.barrel = barrel;
            this.breach = breach;
            this.core = core;
            this.stock = stock;
        }

        public static DynamicGun.Composition fromJson(JsonObject json) {
            DynamicGun.Composition.Part barrel;
            DynamicGun.Composition.Part breach;
            DynamicGun.Composition.Part core;
            DynamicGun.Composition.Part stock;

            if (json.has("barrel")) {
                barrel = DynamicGun.Composition.Part.fromJson(json.getAsJsonObject("barrel"));
            } else {
                barrel = new DynamicGun.Composition.Part();
            }
            if (json.has("breach")) {
                breach = DynamicGun.Composition.Part.fromJson(json.getAsJsonObject("breach"));
            } else {
                breach = new DynamicGun.Composition.Part();
            }
            if (json.has("core")) {
                core = DynamicGun.Composition.Part.fromJson(json.getAsJsonObject("core"));
            } else {
                core = new DynamicGun.Composition.Part();
            }
            if (json.has("stock")) {
                stock = DynamicGun.Composition.Part.fromJson(json.getAsJsonObject("stock"));
            } else {
                stock = new DynamicGun.Composition.Part();
            }

            return new DynamicGun.Composition(barrel, breach, core, stock);
        }

        public static DynamicGun.Composition fromPacket(FriendlyByteBuf buf) {
            ResourceLocation barrel = buf.readResourceLocation();
            ResourceLocation breach = buf.readResourceLocation();
            ResourceLocation core = buf.readResourceLocation();
            ResourceLocation stock = buf.readResourceLocation();
            ResourceLocation part = new ResourceLocation("gunsmoke:gun_parts");
            return new DynamicGun.Composition(
                    new DynamicGun.Composition.Part(part, barrel),
                    new DynamicGun.Composition.Part(part, breach),
                    new DynamicGun.Composition.Part(part, core),
                    new DynamicGun.Composition.Part(part, stock)
            );
        }

        public static DynamicGun.Composition fromMaterials(ResourceLocation gun, ResourceLocation barrel, ResourceLocation breach, ResourceLocation core, ResourceLocation stock) {
            DynamicGun gunInst = Guns.getGun(gun);
            return new DynamicGun.Composition(
                    new DynamicGun.Composition.Part(gunInst.getComposition().getBarrel().getPart(), barrel),
                    new DynamicGun.Composition.Part(gunInst.getComposition().getBreach().getPart(), breach),
                    new DynamicGun.Composition.Part(gunInst.getComposition().getCore().getPart(), core),
                    new DynamicGun.Composition.Part(gunInst.getComposition().getStock().getPart(), stock)
            );
        }

        public static class Part {
            private final ResourceLocation part;
            private final ResourceLocation material;

            public Part() {
                part = new ResourceLocation("gunsmoke:gun_parts");
                material = new ResourceLocation("minecraft:iron");
            }

            private Part(ResourceLocation part, ResourceLocation material) {
                this.part = part;
                this.material = material;
            }

            public static DynamicGun.Composition.Part fromJson(JsonObject json) {
                ResourceLocation part;
                ResourceLocation material;

                if (json.has("part")) {
                    part = new ResourceLocation(json.get("part").getAsString());
                } else {
                    part = new ResourceLocation("gunsmoke:parts");
                }
                if (json.has("default_material")) {
                    material = new ResourceLocation(json.get("default_material").getAsString());
                } else {
                    material = new ResourceLocation("minecraft:iron");
                }

                return new DynamicGun.Composition.Part(part, material);
            }

            public ResourceLocation getPart() {
                return part;
            }

            public ResourceLocation getMaterial() {
                return material;
            }
        }

        public DynamicGun.Composition.Part getBarrel() {
            return barrel;
        }

        public DynamicGun.Composition.Part getBreach() {
            return breach;
        }

        public DynamicGun.Composition.Part getCore() {
            return core;
        }

        public DynamicGun.Composition.Part getStock() {
            return stock;
        }
    }

    public static class RoundStorage {
        private final int capacity;
        private final List<RoundStorage.Round> rounds;

        public RoundStorage() {
            capacity = 1;
            rounds = new ArrayList<>();
            rounds.add(new RoundStorage.Caliber());
        }

        private RoundStorage(int capacity, List<RoundStorage.Round> rounds) {
            this.capacity = capacity;
            this.rounds = rounds;
        }

        public static RoundStorage fromJson(JsonObject json) {
            int capacity = 1;
            List<RoundStorage.Round> rounds = new ArrayList<>();

            if (json.has("capacity")) {
                capacity = json.get("capacity").getAsInt();
            }
            if (json.has("rounds")) {
                for (JsonElement element : json.getAsJsonArray("rounds")) {
                    rounds.add(RoundStorage.Round.fromJson(element.getAsJsonObject()));
                }
            }
            if (rounds.isEmpty()) {
                rounds.add(new RoundStorage.Caliber());
            }

            return new RoundStorage(capacity, rounds);
        }

        public int getCapacity() {
            return capacity;
        }

        public List<RoundStorage.Round> getRounds() {
            return rounds;
        }

        public interface Round {
            static RoundStorage.Round fromJson(JsonObject json) {
                String type = "";
                if (json.has("type")) {
                    type = json.get("type").getAsString();
                }
                if (Objects.equals(type, "caliber")) {
                    return RoundStorage.Caliber.fromJson(json);
                }
                if (Objects.equals(type, "magazine")) {
                    return RoundStorage.Magazine.fromJson(json);
                }
                if (Objects.equals(type, "magazine_family")) {
                    return RoundStorage.MagazineFamily.fromJson(json);
                }
                return null;
            }

            boolean overwritesCapacity();
            int getReloadAmount();
            int getReloadTime();
            ResourceLocation getReloadSound();
        }

        public static class Caliber implements RoundStorage.Round {
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

                return new RoundStorage.Caliber(caliber, reloadAmount, reloadTime, reloadSound);
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

        public static class Magazine implements RoundStorage.Round {
            private final ResourceLocation magazine;
            private final int reloadTime;
            private final ResourceLocation reloadSound;

            private Magazine(ResourceLocation magazine, int reloadTime, ResourceLocation reloadSound) {
                this.magazine = magazine;
                this.reloadTime = reloadTime;
                this.reloadSound = reloadSound;
            }

            public static Magazine fromJson(JsonObject json) {
                ResourceLocation magazine = new ResourceLocation(json.get("magazine").getAsString());
                int reloadTime = 10;
                ResourceLocation reloadSound;

                if (json.has("reload_time")) {
                    reloadTime = json.get("reload_time").getAsInt();
                }
                if (json.has("reload_sound")) {
                    reloadSound = new ResourceLocation(json.get("reload_sound").getAsString());
                } else {
                    reloadSound = new ResourceLocation("cgm:item.pistol.reload");
                }

                return new Magazine(magazine, reloadTime, reloadSound);
            }

            @Override
            public boolean overwritesCapacity() {
                return true;
            }

            @Override
            public int getReloadAmount() {
                return 1;
            }

            @Override
            public int getReloadTime() {
                return reloadTime;
            }

            @Override
            public ResourceLocation getReloadSound() {
                return reloadSound;
            }

            public ResourceLocation getMagazine() {
                return magazine;
            }
        }

        public static class MagazineFamily implements RoundStorage.Round {
            private final ResourceLocation family;
            private final int reloadTime;
            private final ResourceLocation reloadSound;

            private MagazineFamily(ResourceLocation family, int reloadTime, ResourceLocation reloadSound) {
                this.family = family;
                this.reloadTime = reloadTime;
                this.reloadSound = reloadSound;
            }

            public static MagazineFamily fromJson(JsonObject json) {
                ResourceLocation family = new ResourceLocation(json.get("family").getAsString());
                int reloadTime = 10;
                ResourceLocation reloadSound;

                if (json.has("reload_time")) {
                    reloadTime = json.get("reload_time").getAsInt();
                }
                if (json.has("reload_sound")) {
                    reloadSound = new ResourceLocation(json.get("reload_sound").getAsString());
                } else {
                    reloadSound = new ResourceLocation("cgm:item.pistol.reload");
                }

                return new MagazineFamily(family, reloadTime, reloadSound);
            }

            @Override
            public boolean overwritesCapacity() {
                return true;
            }

            @Override
            public int getReloadAmount() {
                return 1;
            }

            @Override
            public int getReloadTime() {
                return reloadTime;
            }

            @Override
            public ResourceLocation getReloadSound() {
                return reloadSound;
            }

            public ResourceLocation getFamily() {
                return family;
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

        public static DynamicGun.Sounds fromJson(JsonObject json) {
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

            return new DynamicGun.Sounds(cock, enchantedFire, fire, silencedFire);
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
        private final DynamicGun.Render.ToolTip toolTip;
        private final DynamicGun.Attachments attachments;
        private final DynamicGun.Attachments.Attachment muzzleFlash;
        private final DynamicGun.Render.IronSights ironSights;

        public Render() {
            model = new ResourceLocation("toolshed:error");
            toolTip = new DynamicGun.Render.ToolTip();
            attachments = new DynamicGun.Attachments();
            muzzleFlash = new DynamicGun.Attachments.Attachment();
            ironSights = new DynamicGun.Render.IronSights();
        }

        private Render(ResourceLocation model, DynamicGun.Render.ToolTip toolTip, DynamicGun.Attachments attachments, DynamicGun.Attachments.Attachment muzzleFlash, DynamicGun.Render.IronSights ironSights) {
            this.model = model;
            this.toolTip = toolTip;
            this.attachments = attachments;
            this.muzzleFlash = muzzleFlash;
            this.ironSights = ironSights;
        }

        public static DynamicGun.Render fromJson(JsonObject json) {
            ResourceLocation model;
            DynamicGun.Render.ToolTip toolTip;
            DynamicGun.Attachments attachments;
            DynamicGun.Attachments.Attachment muzzleFlash;
            DynamicGun.Render.IronSights ironSights;

            if (json.has("model")) {
                model = new ResourceLocation(json.get("model").getAsString());
            } else {
                model = new ResourceLocation("gunsmoke:special/error");
            }
            if (json.has("tool_tip")) {
                toolTip = DynamicGun.Render.ToolTip.fromJson(json.getAsJsonObject("tool_tip"));
            } else {
                toolTip = new DynamicGun.Render.ToolTip();
            }
            if (json.has("attachments")) {
                attachments = DynamicGun.Attachments.fromJson(json.getAsJsonObject("attachments"));
            } else {
                attachments = new DynamicGun.Attachments();
            }
            if (json.has("muzzle_flash")) {
                muzzleFlash = DynamicGun.Attachments.Attachment.fromJson(json.getAsJsonObject("muzzle_flash"));
            } else {
                muzzleFlash = new DynamicGun.Attachments.Attachment();
            }
            if (json.has("iron_sight")) {
                ironSights = DynamicGun.Render.IronSights.fromJson(json.getAsJsonObject("iron_sight"));
            } else {
                ironSights = new DynamicGun.Render.IronSights();
            }

            return new DynamicGun.Render(model, toolTip, attachments, muzzleFlash, ironSights);
        }

        public static DynamicGun.Render fromPacket(GunRenderPacket packet) {
            ResourceLocation model = packet.model;
            DynamicGun.Render.ToolTip toolTip = new DynamicGun.Render.ToolTip();
            DynamicGun.Attachments attachments = new DynamicGun.Attachments();
            DynamicGun.Attachments.Attachment muzzleFlash = new DynamicGun.Attachments.Attachment();
            DynamicGun.Render.IronSights ironSights = new DynamicGun.Render.IronSights();
            return new DynamicGun.Render(model, toolTip, attachments, muzzleFlash, ironSights);
        }

        public ResourceLocation getModel() {
            return model;
        }

        public DynamicGun.Render.ToolTip getToolTip() {
            return toolTip;
        }

        public DynamicGun.Attachments getAttachments() {
            return attachments;
        }

        public DynamicGun.Attachments.Attachment getMuzzleFlash() {
            return muzzleFlash;
        }

        public DynamicGun.Render.IronSights getIronSights() {
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

            public static DynamicGun.Render.ToolTip fromJson(JsonObject json) {
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

                return new DynamicGun.Render.ToolTip(barrel, breach, core, stock);
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

            public static DynamicGun.Render.IronSights fromJson(JsonObject json) {
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

                return new DynamicGun.Render.IronSights(camera, fov);
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
        private final DynamicGun.Attachments.Attachment barrel;
        private final DynamicGun.Attachments.Attachment scope;
        private final DynamicGun.Attachments.Attachment stock;
        private final DynamicGun.Attachments.Attachment underBarrel;

        public Attachments() {
            barrel = new DynamicGun.Attachments.Attachment();
            scope = new DynamicGun.Attachments.Attachment();
            stock = new DynamicGun.Attachments.Attachment();
            underBarrel = new DynamicGun.Attachments.Attachment();
        }

        private Attachments(DynamicGun.Attachments.Attachment barrel, DynamicGun.Attachments.Attachment scope, DynamicGun.Attachments.Attachment stock, DynamicGun.Attachments.Attachment underBarrel) {
            this.barrel = barrel;
            this.scope = scope;
            this.stock = stock;
            this.underBarrel = underBarrel;
        }

        public static DynamicGun.Attachments fromJson(JsonObject json) {
            DynamicGun.Attachments.Attachment barrel;
            DynamicGun.Attachments.Attachment scope;
            DynamicGun.Attachments.Attachment stock;
            DynamicGun.Attachments.Attachment underBarrel;

            if (json.has("barrel")) {
                barrel = DynamicGun.Attachments.Attachment.fromJson(json.getAsJsonObject("barrel"));
            } else {
                barrel = new DynamicGun.Attachments.Attachment();
            }
            if (json.has("scope")) {
                scope = DynamicGun.Attachments.Attachment.fromJson(json.getAsJsonObject("scope"));
            } else {
                scope = new DynamicGun.Attachments.Attachment();
            }
            if (json.has("stock")) {
                stock = DynamicGun.Attachments.Attachment.fromJson(json.getAsJsonObject("stock"));
            } else {
                stock = new DynamicGun.Attachments.Attachment();
            }
            if (json.has("under_barrel")) {
                underBarrel = DynamicGun.Attachments.Attachment.fromJson(json.getAsJsonObject("under_barrel"));
            } else {
                underBarrel = new DynamicGun.Attachments.Attachment();
            }

            return new DynamicGun.Attachments(barrel, scope, stock, underBarrel);
        }

        public DynamicGun.Attachments.Attachment getBarrel() {
            return barrel;
        }

        public DynamicGun.Attachments.Attachment getScope() {
            return scope;
        }

        public DynamicGun.Attachments.Attachment getStock() {
            return stock;
        }

        public DynamicGun.Attachments.Attachment getUnderBarrel() {
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

            public static DynamicGun.Attachments.Attachment fromJson(JsonObject json) {
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

                return new DynamicGun.Attachments.Attachment(translation, scale);
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