package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.minecraft.util.StringRepresentable;

public enum RoundCalibers implements StringRepresentable {
    R22(
            new CaliberProperties("r22")
                    .setBottlenose(
                            new RoundProperties(4.0)
                    )
    ),
    R11_3X36MMR (
            new CaliberProperties("r11_3x36mmr")
                    .setBottlenose(
                            new RoundProperties(6.7)
                                    .setPowder(true)
                    )
    ),
    R600_NITRO(
            new CaliberProperties("r600_nirto")
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
        return this.properties.getName();
    }

    public CaliberProperties getCaliber() {
        return this.properties;
    }
}
