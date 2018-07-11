package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	private final PlayerRepository repository;

	@Autowired
	public SalvoApplication(PlayerRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(){
		return (args) -> {
			repository.save(new Player("j.bauer@ctu.gov.co"));
			repository.save(new Player("c.obrian@ctu.gov.co"));
			repository.save(new Player("kim_bauer@gmail.com"));
			repository.save(new Player("t.almeida@ctu.gov.co"));
		};
	}
}