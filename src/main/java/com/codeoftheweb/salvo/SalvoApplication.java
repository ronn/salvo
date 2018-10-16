package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo){
		return args -> {
			playerRepo.save(new Player("j.bauer@ctu.com"));
			playerRepo.save(new Player("c.obria@ctu.com"));
			playerRepo.save(new Player("k.imbau@ctu.com"));
			playerRepo.save(new Player("t.almei@ctu.com"));
		};
	}
}