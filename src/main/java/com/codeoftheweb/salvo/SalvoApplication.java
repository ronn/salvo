package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.repo.GamePlayerRepository;
import com.codeoftheweb.salvo.repo.GameRepository;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@SpringBootApplication
@ComponentScan
public class SalvoApplication {

	private final PlayerRepository playerRepo;
	private final GameRepository gameRepo;
	private final GamePlayerRepository gamePlayerRepo;

	@Autowired
	public SalvoApplication(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo) {
		this.playerRepo = playerRepo;
		this.gameRepo = gameRepo;
		this.gamePlayerRepo = gamePlayerRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initPlayerData(){
		return args -> {
			final Instant i = LocalDateTime.now().toInstant(ZoneOffset.UTC);

			final Player player = playerRepo.save(new Player("j.bauer@ctu.gov.co"));
			final Player player1 = playerRepo.save(new Player("c.obrian@ctu.gov.co"));
			final Player player2 = playerRepo.save(new Player("kim_bauer@gmail.com"));
			final Player player3 = playerRepo.save(new Player("t.almeida@ctu.gov.co"));

			final Game game = gameRepo.save(new Game(Date.from(i)));
			final Game game1 = gameRepo.save(new Game(Date.from(i.plusSeconds(3600))));
			final Game game2 = gameRepo.save(new Game(Date.from(i.plusSeconds(7200))));

			final GamePlayer gamePlayer = new GamePlayer(new Date(), player, game);
			final GamePlayer gamePlayer1 = new GamePlayer(new Date(), player1, game1);
			final GamePlayer gamePlayer2 = new GamePlayer(new Date(), player2, game2);
			final GamePlayer gamePlayer3 = new GamePlayer(new Date(), player3, game2);
			final GamePlayer gamePlayer4 = new GamePlayer(new Date(), player1, game);
			final GamePlayer gamePlayer5 = new GamePlayer(new Date(), player2, game1);

			gamePlayerRepo.save(gamePlayer);
			gamePlayerRepo.save(gamePlayer1);
			gamePlayerRepo.save(gamePlayer2);
			gamePlayerRepo.save(gamePlayer3);
			gamePlayerRepo.save(gamePlayer4);
			gamePlayerRepo.save(gamePlayer5);
		};
	}

	@Bean
	public CommandLineRunner initGameData(){
		final Instant i = LocalDateTime.now().toInstant(ZoneOffset.UTC);

		return args -> {
			gameRepo.save(new Game(Date.from(i)));
			gameRepo.save(new Game(Date.from(i.plusSeconds(3600))));
			gameRepo.save(new Game(Date.from(i.plusSeconds(7200))));
		};
	}
}