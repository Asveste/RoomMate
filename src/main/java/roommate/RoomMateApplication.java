package roommate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoomMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomMateApplication.class, args);
    }

//    @Bean
//    CommandLineRunner main(WorkspaceRepository repository) {
//        return args -> {
//            System.out.println();
//            System.err.println("Liste aller Workspaces ist am Anfang leer");
//            repository.findAll().forEach(System.err::println);
//
//            System.err.println("Einfügen von Daten");
//            Workspace workspace1 = repository.save(new Workspace(1, UUID.randomUUID()));
//            Workspace workspace2 = repository.save(new Workspace(2, UUID.randomUUID()));
//            Workspace workspace3 = repository.save(new Workspace(3, UUID.randomUUID()));
//            Workspace workspace4 = repository.save(new Workspace(4, UUID.randomUUID()));
//
//            Trait trait1 = new Trait("USB-C");
//            Trait trait2 = new Trait("Monitor");
//            Trait trait3 = new Trait("HDMI");
//            Trait trait4 = new Trait("Beamer");
//            Trait trait5 = new Trait("Poster von David Hasselhoff");
//            Trait trait6 = new Trait("Fax-Gerät");
//
//            workspace1.addTraitAdmin(trait1);
//            workspace1.addTraitAdmin(trait3);
//            workspace2.addTraitAdmin(trait4);
//            workspace2.addTraitAdmin(trait1);
//            workspace3.addTraitAdmin(trait2);
//            workspace3.addTraitAdmin(trait5);
//            workspace3.addTraitAdmin(trait6);
//
//            repository.save(workspace1);
//            repository.save(workspace2);
//
//            System.err.println("Liste aller Workspaces enthält die gespeicherten Aggregate");
//            repository.findAll().forEach(System.err::println);
//
//
//            System.err.println("Eine Workspace kann gesucht werden");
//            Workspace workspace = repository.findById(1).get();
//            System.err.println(workspace);
//
//            List<Workspace> l = repository.findAll().stream().toList();
//
//        };
//    }
}
