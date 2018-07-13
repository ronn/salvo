package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repo.*;
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
	private final SalvoRepository salvoRepo;
	private final ScoreRepository scoreRepo;

	@Autowired
	public SalvoApplication(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo) {
		this.playerRepo = playerRepo;
		this.gameRepo = gameRepo;
		this.gamePlayerRepo = gamePlayerRepo;
		this.shipRepo = shipRepo;
		this.salvoRepo = salvoRepo;
		this.scoreRepo = scoreRepo;
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

			saveScores(player, player1, player2, player3, game, game1, game2);

			final Ship ship1 = shipRepo.save(new Ship(Ship.Type.Carrier));
			final Ship ship2 = shipRepo.save(new Ship(Ship.Type.Battleship));
			final Ship ship3 = shipRepo.save(new Ship(Ship.Type.Carrier));
			final Ship ship4 = shipRepo.save(new Ship(Ship.Type.Battleship));
			final Ship ship5 = shipRepo.save(new Ship(Ship.Type.Submarine));
			final Ship ship6 = shipRepo.save(new Ship(Ship.Type.Destroyer));
			final Ship ship7 = shipRepo.save(new Ship(Ship.Type.Patrol_Boat));
			final Ship ship8 = shipRepo.save(new Ship(Ship.Type.Submarine));
			final Ship ship9 = shipRepo.save(new Ship(Ship.Type.Destroyer));
			final Ship ship = shipRepo.save(new Ship(Ship.Type.Carrier));

			final Salvo salvo = salvoRepo.save(new Salvo(1));
			final Salvo salvo1 = salvoRepo.save(new Salvo(1));
			final Salvo salvo2 = salvoRepo.save(new Salvo(2));
			final Salvo salvo3 = salvoRepo.save(new Salvo(2));
			final Salvo salvo4 = salvoRepo.save(new Salvo(3));
			final Salvo salvo5 = salvoRepo.save(new Salvo(3));
			final Salvo salvo6 = salvoRepo.save(new Salvo(4));
			final Salvo salvo7 = salvoRepo.save(new Salvo(4));
			final Salvo salvo8 = salvoRepo.save(new Salvo(5));
			final Salvo salvo9 = salvoRepo.save(new Salvo(5));

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

			gamePlayer5.addSalvo(salvo1);
			gamePlayer4.addSalvo(salvo2);
			gamePlayer3.addSalvo(salvo3);
			gamePlayer2.addSalvo(salvo4);
			gamePlayer1.addSalvo(salvo5);
			gamePlayer2.addSalvo(salvo6);
			gamePlayer3.addSalvo(salvo7);
			gamePlayer4.addSalvo(salvo8);
			gamePlayer5.addSalvo(salvo9);
			gamePlayer1.addSalvo(salvo);

			saveGamePlayers(Arrays.asList(gamePlayer, gamePlayer1, gamePlayer2, gamePlayer3, gamePlayer4, gamePlayer5));

			saveShips(Arrays.asList(ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9, ship));

			saveSalvos(Arrays.asList(salvo, salvo1, salvo2, salvo3, salvo4, salvo5, salvo6, salvo7, salvo8, salvo9));
		};
	}

	private void saveGamePlayers(List<GamePlayer> gamePlayers) {
		gamePlayerRepo.saveAll(gamePlayers);
	}

	private void saveShips(List<Ship> ships) {
		shipRepo.saveAll(ships);
	}

	private void saveSalvos(List<Salvo> salvoes) {
		salvoRepo.saveAll(salvoes);
	}

	private void saveScores(Player player, Player player1, Player player2, Player player3, Game game, Game game1, Game game2) {
		scoreRepo.saveAll(Arrays.asList(
				new Score(1.0, new Date(), game, player)
				,new Score(0.0, new Date(), game, player1)
				,new Score(0.5, new Date(), game1, player2)
				,new Score(0.5, new Date(), game1, player3)
				,new Score(1.0, new Date(), game2, player)
				,new Score(0.0, new Date(), game2, player3)
		));
	}
}