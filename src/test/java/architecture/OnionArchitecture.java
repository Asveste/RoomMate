package architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import roommate.RoomMateApplication;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;


@AnalyzeClasses(packagesOf = RoomMateApplication.class)
public class OnionArchitecture {

    @ArchTest
    ArchRule rule = onionArchitecture()
            .domainModels("roommate.domain..")
            .domainServices("roommate.applicationservice..")
            .applicationServices("roommate.applicationservice..")
            .adapter("web", "roommate.adapter.web")
            .adapter("persistence", "roommate.adapter.db");

}
