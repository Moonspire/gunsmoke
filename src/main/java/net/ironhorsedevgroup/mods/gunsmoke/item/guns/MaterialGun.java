package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import com.mrcrayfish.guns.common.GripType;
import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.Magazine;
import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.Magazines;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class MaterialGun implements Gun {
    private final ResourceLocation gun;
    private final Magazine magazine;
    private final DynamicGun.Composition composition;
    private final int barrel_damage;
    private final int breach_damage;
    private final int core_damage;
    private final int stock_damage;

    private MaterialGun(ResourceLocation gun, Magazine magazine, ResourceLocation barrelMaterial, int barrelDamage, ResourceLocation breachMaterial, int breachDamage, ResourceLocation coreMaterial, int coreDamage, ResourceLocation stockMaterial, int stockDamage) {
        this.gun = gun;
        this.magazine = magazine;
        composition = DynamicGun.Composition.fromMaterials(gun, barrelMaterial, breachMaterial, coreMaterial, stockMaterial);
        barrel_damage = barrelDamage;
        breach_damage = breachDamage;
        core_damage = coreDamage;
        stock_damage = stockDamage;
    }

    public static Gun fromItemStack(ItemStack itemStack) {
        ResourceLocation gun = NBT.getLocationTag(itemStack, "gun");
        Magazine magazine = Magazines.getMagazine(itemStack);
        ResourceLocation barrel_material = NBT.getLocationTag(itemStack, "barrel");
        int barrel_damage = NBT.getIntTag(itemStack, "barrel_damage");
        ResourceLocation breach_material = NBT.getLocationTag(itemStack, "breach");
        int breach_damage = NBT.getIntTag(itemStack, "breach_damage");
        ResourceLocation core_material = NBT.getLocationTag(itemStack, "core");
        int core_damage = NBT.getIntTag(itemStack, "core_damage");
        ResourceLocation stock_material = NBT.getLocationTag(itemStack, "stock");
        int stock_damage = NBT.getIntTag(itemStack, "stock_damage");

        if  (
                !barrel_material.equals(new ResourceLocation("")) &&
                !breach_material.equals(new ResourceLocation("")) &&
                !core_material.equals(new ResourceLocation("")) &&
                !stock_material.equals(new ResourceLocation(""))
            ) {
            return new MaterialGun(
                    gun,
                    magazine,
                    barrel_material,
                    barrel_damage,
                    breach_material,
                    breach_damage,
                    core_material,
                    core_damage,
                    stock_material,
                    stock_damage
            );
        }
        return Guns.getGun(gun);
    }

    @Override
    public DynamicGun.Properties getProperties() {
        return Guns.getGun(this.gun).getProperties();
    }

    @Override
    public DynamicGun.Composition getComposition() {
        return composition;
    }

    @Override
    public Magazine getMagazine() {
        return magazine;
    }

    @Override
    public DynamicGun.RoundStorage getRoundStorage() {
        return Guns.getGun(this.gun).getRoundStorage();
    }

    @Override
    public DynamicGun.Sounds getSounds() {
        return Guns.getGun(this.gun).getSounds();
    }

    @Override
    public DynamicGun.Render getRender() {
        return Guns.getGun(this.gun).getRender();
    }

    @Override
    public boolean loadRound(Round round) {
        if (magazine.canReloadInGun()) {
            return magazine.loadRound(round);
        }
        return false;
    }

    @Override
    public com.mrcrayfish.guns.common.Gun asGun() {
        DynamicGun gun = Guns.getGun(this.gun);

        com.mrcrayfish.guns.common.Gun.Builder builder = com.mrcrayfish.guns.common.Gun.Builder.create();

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

        return builder.build();
    }
}
