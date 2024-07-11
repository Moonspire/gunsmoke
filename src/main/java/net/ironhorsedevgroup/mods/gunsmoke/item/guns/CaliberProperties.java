package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

public class CaliberProperties {
    private final String caliber;
    private RoundProperties bottlenose = null;
    private RoundProperties pointed = null;
    private RoundProperties armorpiercing = null;
    private RoundProperties hollowpoint = null;
    private RoundProperties caseless = null;
    private RoundProperties slug = null;
    private RoundProperties buckshot = null;
    private RoundProperties birdshot = null;

    public CaliberProperties(String caliber) {
        this.caliber = caliber;
    }

    public CaliberProperties setBottlenose(RoundProperties properties) {
        this.bottlenose = properties;
        return this;
    }

    public RoundProperties getBottlenose() {
        return this.bottlenose;
    }

    public boolean hasBottlenose() {
        return (this.bottlenose != null);
    }

    public CaliberProperties setPointed(RoundProperties properties) {
        this.pointed = properties;
        return this;
    }

    public RoundProperties getPointed() {
        return this.pointed;
    }

    public boolean hasPointed() {
        return (this.pointed != null);
    }

    public CaliberProperties setArmorPiercing(RoundProperties properties) {
        this.armorpiercing = properties;
        return this;
    }

    public RoundProperties getArmorPiercing() {
        return this.armorpiercing;
    }

    public boolean hasArmorPiercing() {
        return (this.armorpiercing != null);
    }

    public CaliberProperties setHollowPoint(RoundProperties properties) {
        this.hollowpoint = properties;
        return this;
    }

    public RoundProperties getHollowPoint() {
        return this.hollowpoint;
    }

    public boolean hasHollowPoint() {
        return (this.hollowpoint != null);
    }

    public CaliberProperties setCaseless(RoundProperties properties) {
        this.caseless = properties;
        return this;
    }

    public RoundProperties getCaseless() {
        return this.caseless;
    }

    public boolean hasCaseless() {
        return (this.caseless != null);
    }

    public CaliberProperties setSlug(RoundProperties properties) {
        this.slug = properties;
        return this;
    }

    public RoundProperties getSlug() {
        return this.slug;
    }

    public boolean hasSlug() {
        return (this.slug != null);
    }

    public CaliberProperties setBuckshot(RoundProperties properties) {
        this.buckshot = properties;
        return this;
    }

    public RoundProperties getBuckshot() {
        return this.buckshot;
    }

    public boolean hasBuckshot() {
        return (this.buckshot != null);
    }

    public CaliberProperties setBirdshot(RoundProperties properties) {
        this.birdshot = properties;
        return this;
    }

    public RoundProperties getBirdshot() {
        return this.birdshot;
    }

    public boolean hasBirdshot() {
        return (this.birdshot != null);
    }

    public String getName() {
        return this.caliber;
    }
}
