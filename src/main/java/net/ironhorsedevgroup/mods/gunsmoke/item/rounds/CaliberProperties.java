package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CaliberProperties {
    private final String caliber;
    private final List<RoundProperties> rounds = new ArrayList<>();

    public CaliberProperties(String caliber) {
        this.caliber = caliber;
    }

    public CaliberProperties addRound(RoundProperties round) {
        this.rounds.add(round.getId(), round);
        return this;
    }

    public RoundProperties getRound(int id) {
        return this.rounds.get(id);
    }

    public List<RoundProperties> getRounds() {
        return this.rounds;
    }

    public String getName() {
        return this.caliber;
    }

    public String getCaliber() {
        return this.caliber;
    }

    public Boolean isCaliber(CaliberProperties caliber) {
        return Objects.equals(this.caliber, caliber.getCaliber());
    }
}
