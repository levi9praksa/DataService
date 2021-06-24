package com.example.controllers;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repository.CurrencyRepository;

import model.Currency;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
@EnableScheduling
public class CurrencyController {

	@Autowired
	CurrencyRepository cr;

	@RequestMapping("/putCurrencies")
	@PostConstruct
	@Scheduled(cron = "0 * * ? * *")
	public String putCurrencies() {

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url("https://api.coincap.io/v2/assets/").method("GET", null).build();
		Response response;
		try {
			response = client.newCall(request).execute();
			String line = response.body().string();
			String[] tokens = line.split("[},:{]");

			int i = 0;
			int j = 0;
			int id = 0;

			Currency c = new Currency();

			for (String t : tokens) {
				String[] s = t.split("[\"]");
				if (i == 30) {
					i = 3;
					id++;
					c.setId(id);
					c.setThreshold(0);
					cr.save(c);

					c = new Currency();
				}

				switch (i) {

				case 4:
					c.setName(s[1]);
					break;

				case 8:
					c.setSymbol(s[1]);
					break;

				case 20:
					c.setPriceusd(Float.parseFloat(s[1]));
					break;

				case 22:
					c.setChangepercent24h(Float.parseFloat(s[1]));
					break;

				default:
					break;

				}

				if (j == 300)
					break;
				i++;
				j++;
			}

			System.out.print("Storing data into database completed.");
			return "Storing data into database completed.";

		} catch (IOException e) {
			e.printStackTrace();
			return "Error";
		}

	}
}
