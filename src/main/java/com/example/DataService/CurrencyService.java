package com.example.DataService;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.repository.CurrencyRepository;

import model.Asset;
import model.Currency;

@Service
public class CurrencyService {
	
	private CurrencyRepository cr;
	
	public CurrencyService(CurrencyRepository cr) {
		this.cr = cr;
	}
	
	public void putCurrencies(){

		final String uri = "https://api.coincap.io/v2/assets/";
	    RestTemplate restTemplate = new RestTemplate();
	    Asset result = restTemplate.getForObject(uri, Asset.class);
	    for(int i = 1; i <= 10; i++) {
	    	Currency currency = new Currency();
	    	currency.setId(i);
	    	currency.setName(result.getData()[i].getId());
	    	currency.setSymbol(result.getData()[i].getSymbol());
	    	currency.setPriceusd(Float.parseFloat(result.getData()[i].getPriceUsd()));
	    	currency.setChangepercent24h(Float.parseFloat(result.getData()[i].getChangePercent24Hr()));
	    	currency.setThreshold(0);
	    	cr.save(currency);
	    }	   
	}
} 
