package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.minecraft.util.StringRepresentable;

public enum RoundCalibers implements StringRepresentable {
    R22(
            new CaliberProperties(".22")
                    .setBottlenose(
                            new RoundProperties(4.0)
                    )
    ),
    R11_3X36MMR (
            new CaliberProperties("11.3x36mmR")
                    .setBottlenose(
                            new RoundProperties(6.7)
                                    .setPowder(true)
                    )
    ),
    R600_NIRO(
            new CaliberProperties(".600")
                    .setBuckshot(
                            new RoundProperties(4.0)
                                    .setProjectileAmount(20)
                    )
    );

    private final CaliberProperties properties;

    private RoundCalibers(CaliberProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getSerializedName() {
        return properties.getName();
    }
}
