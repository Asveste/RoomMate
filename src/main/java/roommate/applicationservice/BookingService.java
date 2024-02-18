package roommate.applicationservice;

public class BookingService {
    private final WorkspaceRepository repo;

    public BookingService(WorkspaceRepository repo) {
        this.repo = repo;
    }
}
