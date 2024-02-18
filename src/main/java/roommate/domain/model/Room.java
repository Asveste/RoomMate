package roommate.domain.model;

import java.util.UUID;

public class Room {
    private final UUID uuid;
    private final Integer id;
    private final String name;

    public Room(String name) {
        this.uuid = UUID.randomUUID();
        this.id = null;
        this.name = name;
    }

    public Room(UUID uuid, Integer id, String name) {
        this.uuid = uuid;
        this.id = id;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Room{" +
                "uuid=" + uuid +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
