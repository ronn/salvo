package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.entity.Ship;
import com.codeoftheweb.salvo.repo.GamePlayerRepository;
import com.codeoftheweb.salvo.repo.GameRepository;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import com.codeoftheweb.salvo.repo.ShipRepository;
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
	public CommandLineRunner initData(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo){
		return args -> {
			Player player1 = new Player("j.bauer@ctu.com");
			Player player2 = new Player("c.obria@ctu.com");
			Player player3 = new Player("k.imbau@ctu.com");
			Player player4 = new Player("t.almei@ctu.com");
			playerRepo.save(player1);
			playerRepo.save(player2);
			playerRepo.save(player3);
			playerRepo.save(player4);

			Instant i = Instant.now();
			Game game1 = new Game(Date.from(i));
			Game game2 = new Game(Date.from(i.plusSeconds(3600)));
			Game game3 = new Game(Date.from(i.plusSeconds(7200)));
			gameRepo.save(game1);
			gameRepo.save(game2);
			gameRepo.save(game3);

            List<String> locations1 = Arrays.asList("A2", "B2", "C2", "D2");
            List<String> locations2 = Arrays.asList("C4", "C5", "C6");
            List<String> locations3 = Arrays.asList("F6", "G6", "E6");
            List<String> locations4 = Arrays.asList("F7", "G7", "H7");

            Ship ship1 = new Ship("Bomber", locations1);
            Ship ship2 = new Ship("Destroyer", locations2);
            Ship ship3 = new Ship("Boat", locations3);
            Ship ship4 = new Ship("Submarin", locations4);

            GamePlayer gp1 = new GamePlayer(new Date());
            player1.addGamePlayer(gp1);
            game1.addGamePlayer(gp1);

            GamePlayer gp2 = new GamePlayer(new Date());
            player2.addGamePlayer(gp2);
            game1.addGamePlayer(gp2);

			gp1.addShip(ship1);
			gp1.addShip(ship3);
			gp2.addShip(ship2);
			gp2.addShip(ship4);

            gamePlayerRepo.save(gp1);
            gamePlayerRepo.save(gp2);

            shipRepo.save(ship1);
            shipRepo.save(ship2);
            shipRepo.save(ship3);
            shipRepo.save(ship4);
		};
	}
}