package roommate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@ActiveProfiles({"test"})
public class RoomMateApplicationTest {

    @Test
    void contextLoads() {

    }

}
