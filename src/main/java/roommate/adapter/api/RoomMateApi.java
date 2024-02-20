//package roommate.adapter.api;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import roommate.applicationservice.BookingService;
//import roommate.domain.model.Workspace;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//public class RoomMateApi {
//    private final BookingService bookingService;
//
//    public RoomMateApi(BookingService bookingService) {
//        this.bookingService = bookingService;
//    }
//
//    @GetMapping("api/access")
//    public ResponseEntity<RoomEntity>api1() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//}
