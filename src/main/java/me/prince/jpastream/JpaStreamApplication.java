package me.prince.jpastream;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SpringBootApplication
public class JpaStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaStreamApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(GameRepo gameRepo) {
        return args -> {
            loadCSVfile(gameRepo);
        };
    }

    private void loadCSVfile(GameRepo gameRepo) throws IOException {
        String gameFilePath = "games.csv";
        Files.lines(Paths.get(gameFilePath))
                .map(s -> {
                    String[] splitStr = s.split(",");
                    return new GameEntity(
                            splitStr[0],
                            Integer.parseInt(splitStr[1]),
                            splitStr[2],
                            Integer.parseInt(splitStr[3])
                    );
                }).forEach(gameRepo::save);
    }
}


@Repository
interface GameRepo extends JpaRepository<GameEntity, Long> {

    Stream<GameEntity> streamAll();
}

@Entity
@Data
class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    Integer year;

    String team;

    Integer week;

    public GameEntity(String name, Integer year, String team, Integer week) {
        this.name = name;
        this.year = year;
        this.team = team;
        this.week = week;
    }
}


