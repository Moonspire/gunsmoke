package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.CaliberProperties;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

public enum GunsmokeCalibers implements StringRepresentable {
    G12 (
            new CaliberProperties("g12")

                    //slug
                    .addRound(
                            new RoundProperties(0, 18.0)
                                    .setLife(30)
                                    .setSize(0.1)
                                    .setTexture("gunsmoke:items/rounds/shotgun_shell")
                                    .setColor(Color.getIntFromRGB(65, 105, 179))
                                    .setRoundRender(false)
                    )

                    //buckshot
                    .addRound(
                            new RoundProperties(1, 4.0)
                                    .setLife(10)
                                    .setGravity(false)
                                    .setProjectileAmount(8)
                                    .setSize(0.0625)
                                    .setTexture("gunsmoke:items/rounds/shotgun_shell")
                                    .setColor(Color.getIntFromRGB(182, 32, 32))
                                    .setRoundRender(false)
                    )

                    //birdshot
                    .addRound(
                            new RoundProperties(2, 1.0)
                                    .setLife(5)
                                    .setGravity(false)
                                    .setProjectileAmount(20)
                                    .setSize(0.0625)
                                    .setTexture("gunsmoke:items/rounds/shotgun_shell")
                                    .setColor(Color.getIntFromRGB(29, 185, 154))
                                    .setRoundRender(false)
                    )
    ),
    R44_POWDER (
            new CaliberProperties("r44_powder")

                    .addRound(
                            new RoundProperties(0, 4.0)
                                    .setLife(30)
                                    .setPowder(true)
                                    .setTexture("gunsmoke:items/rounds/powder_ball")
                                    .setCaseless(true)
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

    GunsmokeCalibers(CaliberProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getSerializedName() {
        return this.properties.getName();
    }

    public CaliberProperties getCaliber() {
        return this.properties;
    }

    public static RoundProperties getRound(ItemStack itemStack) {
        return RoundItem.getRound(itemStack);
    }
}
