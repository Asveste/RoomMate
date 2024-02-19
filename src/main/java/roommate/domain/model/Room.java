//package roommate.domain.model;
//
//import java.util.UUID;
//
//public class Room {
//    private final UUID uuid;
//    private final Integer roomId;
//    private final String name;
//
//    public Room(String name) {
//        this.uuid = UUID.randomUUID();
//        this.roomId = null;
//        this.name = name;
//    }
//
//    public Room(UUID uuid, Integer id, String name) {
//        this.uuid = uuid;
//        this.roomId = id;
//        this.name = name;
//    }
//
//    public UUID uuid() {
//        return uuid;
//    }
//
//    public Integer roomId() {
//        return roomId;
//    }
//
//    public String name() {
//        return name;
//    }
//
//    @Override
//    public String toString() {
//        return "Room{" +
//                "uuid=" + uuid +
//                ", id=" + roomId +
//                ", name='" + name + '\'' +
//                '}';
//    }
//}
