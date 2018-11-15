package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo){
		return args -> {
			Player player1 = new Player("j.bauer@ctu.com", "24");
			Player player2 = new Player("c.obria@ctu.com", "42");
			Player player3 = new Player("k.imbau@ctu.com", "kb");
			Player player4 = new Player("t.almei@ctu.com", "mole");

			Instant i = Instant.now();
			Game game1 = new Game(Date.from(i));
			Game game2 = new Game(Date.from(i.plusSeconds(3600)));
			Game game3 = new Game(Date.from(i.plusSeconds(7200)));

            List<String> locations1 = Arrays.asList("A2", "B2", "C2", "D2");
            List<String> locations2 = Arrays.asList("C4", "C5", "C6");
            List<String> locations3 = Arrays.asList("F6", "G6", "E6");
            List<String> locations4 = Arrays.asList("F7", "G7", "H7");

            Ship ship1 = new Ship("Bomber", locations1);
            Ship ship2 = new Ship("Destroyer", locations2);
            Ship ship3 = new Ship("Boat", locations3);
            Ship ship4 = new Ship("Submarin", locations4);

            GamePlayer gp1 = new GamePlayer(new Date());
			GamePlayer gp2 = new GamePlayer(new Date());

			gp1.addShip(ship1);
			gp1.addShip(ship3);
			gp2.addShip(ship2);
			gp2.addShip(ship4);

			List<String> salvoLocations1 = Arrays.asList("A1", "J3", "G7", "E10");
			List<String> salvoLocations2 = Arrays.asList("B6", "C6", "A6", "F9");
			List<String> salvoLocations3 = Arrays.asList("C9", "I1", "E4", "H5");
			List<String> salvoLocations4 = Arrays.asList("D2", "D9", "B2", "G1");

			Salvo salvo1 = new Salvo(1, salvoLocations1);
			Salvo salvo2 = new Salvo(2, salvoLocations2);
			Salvo salvo3 = new Salvo(1, salvoLocations3);
			Salvo salvo4 = new Salvo(2, salvoLocations4);

			gp1.addSalvo(salvo1);
			gp1.addSalvo(salvo2);
			gp2.addSalvo(salvo3);
			gp2.addSalvo(salvo4);

			player1.addGamePlayer(gp1);
			player2.addGamePlayer(gp2);

			game1.addGamePlayer(gp1);
			game1.addGamePlayer(gp2);

			Score score1 = new Score(1.0, new Date());
			Score score2 = new Score(0.5, new Date());
			Score score3 = new Score(1.0, new Date());
			Score score4 = new Score(1.0, new Date());
			Score score5 = new Score(0.0, new Date());
			Score score6 = new Score(0.5, new Date());

			player1.addScore(score1);
			player1.addScore(score3);
			player1.addScore(score6);
			player2.addScore(score2);
			game1.addScore(score1);
			game1.addScore(score2);

			playerRepo.save(player1);
			playerRepo.save(player2);
			playerRepo.save(player3);
			playerRepo.save(player4);

			gameRepo.save(game1);
			gameRepo.save(game2);
			gameRepo.save(game3);

            gamePlayerRepo.save(gp1);
            gamePlayerRepo.save(gp2);

            shipRepo.save(ship1);
            shipRepo.save(ship2);
            shipRepo.save(ship3);
            shipRepo.save(ship4);

            salvoRepo.save(salvo1);
            salvoRepo.save(salvo2);
            salvoRepo.save(salvo3);
            salvoRepo.save(salvo4);

            scoreRepo.save(score1);
            scoreRepo.save(score2);
            scoreRepo.save(score3);
            scoreRepo.save(score6);
		};
	}
}