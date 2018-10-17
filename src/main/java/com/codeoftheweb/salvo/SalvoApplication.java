package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.repo.GamePlayerRepository;
import com.codeoftheweb.salvo.repo.GameRepository;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo){
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

			GamePlayer gp = new GamePlayer(new Date());
			player1.addGamePlayer(gp);
			game2.addGamePlayer(gp);
			gamePlayerRepo.save(gp);
		};
	}
}