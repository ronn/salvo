package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.entity.Ship;
import com.codeoftheweb.salvo.repo.GamePlayerRepository;
import com.codeoftheweb.salvo.repo.GameRepository;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import com.codeoftheweb.salvo.repo.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@ComponentScan
public class SalvoApplication {

	private final PlayerRepository playerRepo;
	private final GameRepository gameRepo;
	private final GamePlayerRepository gamePlayerRepo;
	private final ShipRepository shipRepo;

	@Autowired
	public SalvoApplication(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo) {
		this.playerRepo = playerRepo;
		this.gameRepo = gameRepo;
		this.gamePlayerRepo = gamePlayerRepo;
		this.shipRepo = shipRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(){
		return args -> {
			final Instant i = LocalDateTime.now().toInstant(ZoneOffset.UTC);

			final Player player = playerRepo.save(new Player("j.bauer@ctu.gov.co"));
			final Player player1 = playerRepo.save(new Player("c.obrian@ctu.gov.co"));
			final Player player2 = playerRepo.save(new Player("kim_bauer@gmail.com"));
			final Player player3 = playerRepo.save(new Player("t.almeida@ctu.gov.co"));

			final Game game = gameRepo.save(new Game(Date.from(i)));
			final Game game1 = gameRepo.save(new Game(Date.from(i.plusSeconds(3600))));
			final Game game2 = gameRepo.save(new Game(Date.from(i.plusSeconds(7200))));

			final Ship ship1 = shipRepo.save(new Ship(getLocations(), "Battle"));
			final Ship ship2 = shipRepo.save(new Ship(getLocations(), "Other"));
			final Ship ship3 = shipRepo.save(new Ship(getLocations(), "Type1"));
			final Ship ship4 = shipRepo.save(new Ship(getLocations(), "Crusier"));
			final Ship ship5 = shipRepo.save(new Ship(getLocations(), "Battle"));
			final Ship ship6 = shipRepo.save(new Ship(getLocations(), "Other"));
			final Ship ship7 = shipRepo.save(new Ship(getLocations(), "Type1"));
			final Ship ship8 = shipRepo.save(new Ship(getLocations(), "Crusier"));
			final Ship ship9 = shipRepo.save(new Ship(getLocations(), "Battle"));
			final Ship ship = shipRepo.save(new Ship(getLocations(), "Battle"));

			final GamePlayer gamePlayer = new GamePlayer(new Date(), player, game);
			final GamePlayer gamePlayer1 = new GamePlayer(new Date(), player1, game1);
			final GamePlayer gamePlayer2 = new GamePlayer(new Date(), player2, game2);
			final GamePlayer gamePlayer3 = new GamePlayer(new Date(), player3, game2);
			final GamePlayer gamePlayer4 = new GamePlayer(new Date(), player1, game);
			final GamePlayer gamePlayer5 = new GamePlayer(new Date(), player2, game1);

			gamePlayer1.addShip(ship1);
			gamePlayer2.addShip(ship2);
			gamePlayer3.addShip(ship3);
			gamePlayer4.addShip(ship4);
			gamePlayer5.addShip(ship5);
			gamePlayer1.addShip(ship6);
			gamePlayer3.addShip(ship7);
			gamePlayer4.addShip(ship8);
			gamePlayer2.addShip(ship9);
			gamePlayer5.addShip(ship);

			gamePlayerRepo.save(gamePlayer);
			gamePlayerRepo.save(gamePlayer1);
			gamePlayerRepo.save(gamePlayer2);
			gamePlayerRepo.save(gamePlayer3);
			gamePlayerRepo.save(gamePlayer4);
			gamePlayerRepo.save(gamePlayer5);

			shipRepo.save(ship1);
			shipRepo.save(ship2);
			shipRepo.save(ship3);
			shipRepo.save(ship4);
			shipRepo.save(ship5);
			shipRepo.save(ship6);
			shipRepo.save(ship7);
			shipRepo.save(ship8);
			shipRepo.save(ship9);
			shipRepo.save(ship);
		};
	}

	private List<String> getLocations(){
		return Arrays.asList(
				"F1",
				"A2",
				"D4",
				"S3",
				"T5",
				"F6",
				"D7",
				"S9",
				"A8",
				"S6",
				"E4",
				"D3",
				"S2",
				"A1"
		);
	}
}