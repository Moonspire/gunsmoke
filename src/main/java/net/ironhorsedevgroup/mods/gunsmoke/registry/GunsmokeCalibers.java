package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.CaliberProperties;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.minecraft.util.StringRepresentable;

public enum GunsmokeCalibers implements StringRepresentable {
    G12 (
            new CaliberProperties("g12")

                    //slug
                    .addRound(
                            new RoundProperties(0, 12.0)
                                    .setLife(30)
                                    .setGravity(true)
                                    .setSize(0.1)
                    )

                    //buckshot
                    .addRound(
                            new RoundProperties(1, 4.0)
                                    .setLife(10)
                                    .setGravity(false)
                                    .setProjectileAmount(8)
                                    .setSize(0.0625)
                    )

                    //birdshot
                    .addRound(
                            new RoundProperties(2, 1.0)
                                    .setLife(5)
                                    .setGravity(false)
                                    .setProjectileAmount(20)
                                    .setSize(0.0625)
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
