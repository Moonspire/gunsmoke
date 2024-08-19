package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.CaliberProperties;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.minecraft.util.StringRepresentable;

public enum GunsmokeCalibers implements StringRepresentable {
    R11_3X36MMR (
            new CaliberProperties("r11_3x36mmr")
                    .addRound(
                            new RoundProperties(0,6.7)
                                    .setPowder(true)
                    )
    ),
    R45_70 (
            new CaliberProperties("r45_70")

                    // .45-70
                    .addRound(
                            new RoundProperties(0, 15.0)
                                    .setLife(40)
                                    .setSize(0.0625)
                                    .setSpeed(20.0)
                    )

                    // .45-100
                    .addRound(
                            new RoundProperties(1, 15.0)
                                    .setLife(40)
                                    .setSize(0.0625)
                                    .setSpeed(25.0)
                    )

                    // .45-120
                    .addRound(
                            new RoundProperties(2, 15.0)
                                    .setLife(40)
                                    .setSize(0.0625)
                                    .setSpeed(30.0)
                    )

                    // .45-Birdshot (Purely for testing)
                    .addRound(
                            new RoundProperties(3, 1.0)
                                    .setLife(10)
                                    .setGravity(false)
                                    .setSize(0.0625)
                                    .setProjectileAmount(20)
                                    .setSpeed(10.0)
                    )
    );

    private final CaliberProperties properties;

    private GunsmokeCalibers(CaliberProperties properties) {
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
