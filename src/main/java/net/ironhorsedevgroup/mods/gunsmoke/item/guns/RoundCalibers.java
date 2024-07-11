package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.minecraft.util.StringRepresentable;

public enum RoundCalibers implements StringRepresentable {
    TWENTY_TWO(
            new CaliberProperties(".22")
                    .setBottlenose(
                            new RoundProperties(4.0)
                    )
    ),
    SIX_HUNDRED_NIRO(
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
