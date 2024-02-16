package roommate.domain.model;

public class Trait {
    private String trait;

    public Trait(String trait) {
        this.trait = trait;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String traits) {
        this.trait = traits;
    }

    @Override
    public String toString() {
        return trait;
    }
}
