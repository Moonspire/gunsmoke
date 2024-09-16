package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

public interface Gun {
    com.mrcrayfish.guns.common.Gun asGun();
    DynamicGun.Properties getProperties();
    DynamicGun.Composition getComposition();
    DynamicGun.RoundStorage getMagazine();
    DynamicGun.Sounds getSounds();
    DynamicGun.Render getRender();
}
