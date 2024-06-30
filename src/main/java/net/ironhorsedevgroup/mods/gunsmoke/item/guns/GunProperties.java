package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import com.mrcrayfish.guns.common.GripType;
import com.mrcrayfish.guns.common.Gun;
import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class GunProperties {
    private final Integer reloadWait;
    private Gun result;
    private GunMakeup gunMakeup = new GunMakeup();

    public GunProperties(Gun gun, Integer reload) {
        reloadWait = reload;
        result = buildGun(gun);
    }

    public Integer getPureReloadWait() {
        return reloadWait;
    }

    public Integer getReloadWait(ItemStack itemStack) {
        updateGun(itemStack);
        return calcReload();
    }

    public void damageGun(ItemStack itemStack, Integer damage) {
        updateGun(itemStack);
        gunMakeup.dealDamage(damage);
    }

    public void damageGun(ItemStack itemStack) {
        updateGun(itemStack);
        gunMakeup.dealDamage();
    }

    public Gun getGun(ItemStack itemStack) {
        updateGun(itemStack);
        return result;
    }

    private void updateGun(ItemStack itemStack) {
        GunMakeup newGunMakeup = new GunMakeup(itemStack);
        if (itemStack.getItem() instanceof RifleItem rifleItem && !gunMakeup.is(newGunMakeup)) {
            gunMakeup = newGunMakeup;
            result = buildGun(rifleItem.getGun());
        }
    }

    private Float calcSpread(Float initSpread) {
        return (float)((200 + gunMakeup.getBarrelTotalDamage()) / gunMakeup.getBarrel().getPurity()) * initSpread;
    }

    private Integer calcReload() {
        return ((200 + gunMakeup.getBreachTotalDamage()) / gunMakeup.getBreach().getPurity()) * reloadWait;
    }

    private Integer calcFire(Integer initRate) {
        return (int)(Math.pow(1.1, (gunMakeup.getCore().getHardness() * (gunMakeup.getCoreTotalDamage() / 100.0)) / 6.0) * initRate);
    }

    private Float calcRecoilAngle(Float initRecoilAngle) {
        Integer front = gunMakeup.getBarrel().getDensity();
        if (front == 0) {
            front = 1;
        }
        Integer rear = gunMakeup.getCore().getDensity() + gunMakeup.getStock().getDensity() + gunMakeup.getBreach().getDensity();
        return ((rear / front) - 1) * (initRecoilAngle / 3);
    }

    private Float calcRecoilKick(Float initRecoilKick) {
        Integer density = gunMakeup.getBarrel().getDensity() + gunMakeup.getCore().getDensity() + gunMakeup.getStock().getDensity() + gunMakeup.getBreach().getDensity();
        return (float)((681.0 + gunMakeup.getStockTotalDamage()) / (density + 81)) * initRecoilKick;
    }
    
    private Gun buildGun(Gun gun) {
        try {
            // Display
            Gun.Display display = gun.getDisplay();

            Gun.Display.Flash flash = display.getFlash();

            // General
            Gun.General general = gun.getGeneral();

            Boolean alwaysSpread = general.isAlwaysSpread();
            Boolean auto = general.isAuto();
            GripType gripType = general.getGripType();
            Integer maxAmmo = general.getMaxAmmo();
            Integer rate = general.getRate();
            Float recoilAdsReduction = general.getRecoilAdsReduction();
            Float recoilAngle = general.getRecoilAngle();
            Float recoilKick = general.getRecoilKick();
            Integer reloadAmount = general.getReloadAmount();
            Float spread = general.getSpread();

            // Modules
            Gun.Modules modules = gun.getModules();
            Gun.Modules.Attachments attachments = modules.getAttachments();

            Gun.ScaledPositioned barrel = attachments.getBarrel();
            Gun.ScaledPositioned scope = attachments.getScope();
            Gun.ScaledPositioned stock = attachments.getStock();
            Gun.ScaledPositioned underBarrel = attachments.getUnderBarrel();
            Gun.Modules.Zoom zoom = modules.getZoom();

            // Projectile
            Gun.Projectile projectile = gun.getProjectile();

            Float damage = projectile.getDamage();
            Boolean gravity = projectile.isGravity();
            Item ammoItem = ForgeRegistries.ITEMS.getValue(projectile.getItem());
            Integer life = projectile.getLife();
            Float size = projectile.getSize();
            Double speed = projectile.getSpeed();

            // Sounds
            Gun.Sounds sounds = gun.getSounds();

            SoundEvent cock = ForgeRegistries.SOUND_EVENTS.getValue(sounds.getCock());
            SoundEvent fire = ForgeRegistries.SOUND_EVENTS.getValue(sounds.getFire());
            SoundEvent enchantedFire = ForgeRegistries.SOUND_EVENTS.getValue(sounds.getEnchantedFire());
            SoundEvent silencedFire = ForgeRegistries.SOUND_EVENTS.getValue(sounds.getSilencedFire());
            SoundEvent reload = ForgeRegistries.SOUND_EVENTS.getValue(sounds.getReload());

            spread = calcSpread(spread);
            rate = calcFire(rate);
            recoilAngle = calcRecoilAngle(recoilAngle);
            recoilKick = calcRecoilKick(recoilKick);

            return Gun.Builder.create()
                    // Display
                    .setMuzzleFlash(flash.getSize(), flash.getXOffset(), flash.getYOffset(), flash.getZOffset())

                    // General
                    .setAlwaysSpread(alwaysSpread)
                    .setAuto(auto)
                    .setGripType(gripType)
                    .setMaxAmmo(maxAmmo)
                    .setFireRate(rate)
                    .setRecoilAdsReduction(recoilAdsReduction)
                    .setRecoilAngle(recoilAngle)
                    .setRecoilKick(recoilKick)
                    .setReloadAmount(reloadAmount)
                    .setSpread(spread)

                    // Modules
                    .setBarrel((float) barrel.getScale(), barrel.getXOffset(), barrel.getYOffset(), barrel.getZOffset())
                    .setScope((float) scope.getScale(), scope.getXOffset(), scope.getYOffset(), scope.getZOffset())
                    .setStock((float) stock.getScale(), stock.getXOffset(), stock.getYOffset(), stock.getZOffset())
                    .setUnderBarrel((float) underBarrel.getScale(), underBarrel.getXOffset(), underBarrel.getYOffset(), underBarrel.getZOffset())
                    .setZoom(zoom.getFovModifier(), zoom.getXOffset(), zoom.getYOffset(), zoom.getZOffset())

                    // Projectile
                    .setDamage(damage)
                    .setProjectileAffectedByGravity(gravity)
                    .setAmmo(ammoItem)
                    .setProjectileLife(life)
                    .setProjectileSize(size)
                    .setProjectileSpeed(speed)

                    // Sounds
                    .setCockSound(cock)
                    .setFireSound(fire)
                    .setEnchantedFireSound(enchantedFire)
                    .setSilencedFireSound(silencedFire)
                    .setReloadSound(reload)

                    // Build
                    .build();
        } catch (Exception e) {
            return gun;
        }
    }
}
