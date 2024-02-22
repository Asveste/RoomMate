package roommate.domain.model;

public record Trait(String trait) {
    @Override
    public String toString() {
        return trait;
    }
}
