package com.example.controllers;

import java.io.IOException;
import java.util.List;

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

	private static final int START_OF_CURRENCY = 3;
	private static final int CURRENCY_NAME_LINE = 4;
	private static final int CURRENCY_SYMBOL_LINE = 8;
	private static final int CURRENCY_USDPRICE_LINE = 20;
	private static final int CURRENCY_PERCENTCHANGE_LINE = 22;
	private static final int END_OF_CURRENCY = 30;
	private static final int LAST_LINE = 300;
	
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
				if (i == END_OF_CURRENCY) {
					i = START_OF_CURRENCY;
					id++;
					c.setId(id);
					c.setThreshold(0);
					cr.save(c);

					c = new Currency();
				}

				switch (i) {

				case CURRENCY_NAME_LINE:
					c.setName(s[1]);
					break;

				case CURRENCY_SYMBOL_LINE:
					c.setSymbol(s[1]);
					break;

				case CURRENCY_USDPRICE_LINE:
					c.setPriceusd(Float.parseFloat(s[1]));
					break;

				case CURRENCY_PERCENTCHANGE_LINE:
					c.setChangepercent24h(Float.parseFloat(s[1]));
					break;

				default:
					break;

				}

				if (j == LAST_LINE)
					break;
				i++;
				j++;
			}

			System.out.println("Storing data into database completed.");
			return "Storing data into database completed.";

		} catch (IOException e) {
			e.printStackTrace();
			return "Error";
		}

	}
	
	//Just testing for future
	@RequestMapping("/showCurrencies")
	public String showCrypto() {
		
		StringBuilder sb = new StringBuilder();
		List<Currency> list = cr.findAll();
		for(Currency c : list) {
			
			sb.append(c.getId() + "\t" + c.getName() + "\t" + c.getSymbol() + "\t"  + c.getPriceusd() + "\t" + c.getChangepercent24h() + "\t" + c.getThreshold());
			System.out.println(c.getId() + "\t" + c.getName() + "\t" + c.getSymbol() + "\t"  + c.getPriceusd() + "\t" + c.getChangepercent24h() + "\t" + c.getThreshold());
		}
		

		return sb.toString();
	}
}
