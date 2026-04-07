package com.taller.gestion_taller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class GestionTallerApplication {

	public static void main(String[] args) {
        SpringApplication.run(GestionTallerApplication.class, args);

        log.info("  ___   _  __  _ ");
        log.info(" / _ \\ | |/ / | |");
        log.info("| (_) || ' <  |_|");
        log.info(" \\___/ |_|\\_\\ (_)");
	}

}
