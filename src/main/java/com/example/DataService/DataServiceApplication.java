package com.example.DataService;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repository.CurrencyRepository;

import model.Currency;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SpringBootApplication(scanBasePackages = "com.repository")
@EntityScan("model")
@EnableJpaRepositories("com.repository")
@RestController
public class DataServiceApplication {

	@Autowired
	CurrencyRepository cr;

	public static void main(String[] args) {
		SpringApplication.run(DataServiceApplication.class, args);

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url("https://api.coincap.io/v2/assets/").method("GET", null).build();
		Response response;
		try {
			response = client.newCall(request).execute();
			String line = response.body().string();
			String[] tokens = line.split("[},:{]");

			int i = 0;
			int j = 0;

			for (String t : tokens) {
				if (i == 30) {
					i = 3;
					System.out.println("");
				}

				if (i == 4 || i == 8 | i == 20 | i == 22) {
					String[] s = t.split("[\"]");
					System.out.print(s[1] + " ");
				}
				if (j == 300)
					break;
				i++;
				j++;
			}

		} catch (

		IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }

		}
	}

	@RequestMapping("/putCurrencies")
	@PostConstruct
	public void putCurrencies() {

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

		} catch (

		IOException e) {
			e.printStackTrace();
		}

	}

}
