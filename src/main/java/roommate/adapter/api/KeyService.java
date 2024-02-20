//package roommate.adapter.api;
//
//import com.google.common.collect.HashMultimap;
//import com.google.common.collect.SetMultimap;
//import java.time.Duration;
//import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//import java.util.Map.Entry;
//import java.util.concurrent.locks.ReentrantLock;
//import java.util.stream.Collectors;
////import keymaster.KeymasterApplication;
//import roommate.adapter.api.Access;
//import roommate.adapter.api.Key;
//import roommate.adapter.api.Room;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Service
//public class KeyService {
//    private final WebClient webClient = WebClient.create(System.getenv("ROOMMATE_URL"));
//    private final Logger log = LoggerFactory.getLogger(KeymasterApplication.class);
//    private final ReentrantLock lock = new ReentrantLock();
//    private SetMultimap<UUID, UUID> access = HashMultimap.create();
//    private Set<Key> keys = new HashSet();
//    private Set<Room> rooms = new HashSet();
//
//    @Scheduled(
//            cron = "0 * * * * *"
//    )
//    public void fetch() {
//        System.out.println(LocalTime.now());
//        this.log.info("Updating Access-Information");
//
//        try {
//            List<Access> currentAccess = this.fetchFromRoomMate();
//            SetMultimap<UUID, UUID> newAccessTable = this.createAccessTable(currentAccess);
//            synchronized(this.lock) {
//                this.access = newAccessTable;
//            }
//        } catch (Exception var6) {
//            this.log.error("Something went wrong while fetching Access-Data from Roommate.", var6);
//        }
//
//    }
//
//    private SetMultimap<UUID, UUID> createAccessTable(List<Access> currentAccess) {
//        HashMultimap<UUID, UUID> acl = HashMultimap.create();
//        currentAccess.forEach((access) -> {
//            acl.put(access.key(), access.room());
//        });
//        return acl;
//    }
//
//    private List<Access> fetchFromRoomMate() {
//        return (List)this.webClient.get().uri(System.getenv("ROOMMATE_ENDPOINT"), new Object[0]).retrieve().bodyToFlux(Access.class).collectList().block(Duration.of(3L, ChronoUnit.SECONDS));
//    }
//
//    public List<Key> keys() {
//        return this.keys.stream().sorted(Comparator.comparing(Key::owner)).toList();
//    }
//
//    public List<Room> rooms() {
//        return this.rooms.stream().sorted(Comparator.comparing(Room::raum)).toList();
//    }
//
//    public void addRoom(Room room) {
//        this.rooms.add(room);
//    }
//
//    public void addKey(Key key) {
//        this.keys.add(key);
//    }
//
//    public List<Room> accessForKey(UUID key) {
//        Set allowed;
//        synchronized(this.lock) {
//            allowed = this.access.get(key);
//        }
//
//        return this.rooms.stream().filter((r) -> {
//            return allowed.contains(r.id());
//        }).sorted(Comparator.comparing(Room::raum)).toList();
//    }
//
//    public List<Key> accessForRoom(UUID room) {
//        Set allowedkeys;
//        synchronized(this.lock) {
//            allowedkeys = (Set)this.access.entries().stream().filter((e) -> {
//                return ((UUID)e.getValue()).equals(room);
//            }).map(Entry::getKey).collect(Collectors.toSet());
//        }
//
//        return this.keys().stream().filter((k) -> {
//            return allowedkeys.contains(k.id());
//        }).toList();
//    }
//
//    public String roomName(UUID uuid) {
//        return (String)this.rooms.stream().filter((r) -> {
//            return r.id().equals(uuid);
//        }).findFirst().map(Room::raum).orElseThrow();
//    }
//
//    public String ownerName(UUID uuid) {
//        return (String)this.keys.stream().filter((r) -> {
//            return r.id().equals(uuid);
//        }).findFirst().map(Key::owner).orElseThrow();
//    }
//}
