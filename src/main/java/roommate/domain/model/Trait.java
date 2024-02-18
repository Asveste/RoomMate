package roommate.domain.model;

public class Trait {
    private final Integer id;
    private final String trait;

    public Trait(Integer id, String trait) {
        this.id = id;
        this.trait = trait;
    }

    public Integer getId() {
        return id;
    }

    public String getTrait() {
        return trait;
    }

    @Override
    public String toString() {
        return trait;
    }
}
