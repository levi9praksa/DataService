package com.example.DataService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

import com.repository.CurrencyRepository;

import model.Currency;

@Configuration
public class CurrencyConfig {
	
	@Bean
	@RequestMapping("/putCurrencies")
	CommandLineRunner commandLineRunner(CurrencyRepository repository) {
		return args -> {
			/*Currency c = new Currency();

			c.setId(2);
			c.setName("ethereum");
			c.setSymbol("ETH");
			c.setPriceusd(300);
			c.setChangepercent24h(2);
			c.setThreshold(15);
			repository.save(c);
			
			System.out.print("Done!");*/
		};
	}
}
