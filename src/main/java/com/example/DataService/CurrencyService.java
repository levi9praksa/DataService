package com.example.DataService;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.repository.CurrencyRepository;
import com.example.repository.HistoryRepository;

import lombok.RequiredArgsConstructor;
import model.Asset;
import model.Currency;
import model.History;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class CurrencyService {
	
	@Value("${my.url}")
	private String uri;
	
	final CurrencyRepository cr;
	final HistoryRepository hr;

	private int constraint;
	
	private static final int INTERVAL_INCREASEMENT = 1;
	private static final int STATUS_CODE_CONSTRAINT = 400;
	private static final int DEFAULT_THRESHOLD = 0;
	private static final int LAST_CURRENCY = 10;
	private static final int FIRST_CURRENCY = 1;
		
	public Asset putCurrencies() {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
		
		if (response.getStatusCodeValue() < STATUS_CODE_CONSTRAINT) {

			Asset result = restTemplate.getForObject(uri, Asset.class);

			if (result.getData().length >= LAST_CURRENCY) {
				constraint = LAST_CURRENCY;
			} else {
				constraint = result.getData().length;
			}

			for (int i = FIRST_CURRENCY; i <= constraint; i++) {
				Currency currency = new Currency();
				currency.setId(i);
				int currencyId = i - 1;
				currency.setName(result.getData()[currencyId].getId());
				currency.setSymbol(result.getData()[currencyId].getSymbol());
				currency.setPriceusd(new BigDecimal((result.getData()[currencyId].getPriceUsd())));
				currency.setChangepercent24h(new BigDecimal((result.getData()[currencyId].getChangePercent24Hr())));
				currency.setThreshold(new BigDecimal(DEFAULT_THRESHOLD));
				cr.save(currency);
			}

			return result;
			
		} else {			
			return null;
			
		}
	}
	
	public History getLastHistory() {
		return hr.findAll()
				.stream()
				.skip(hr.findAll().stream().count() - 1)
				.findFirst()
				.get();
	}
	
	
	@Scheduled(cron = "${my.cron}")
	public void updateHistory(){
		
		Asset asset = putCurrencies();
		int lastInterval = getLastHistory().getInterval();
		for(int i = FIRST_CURRENCY; i <= constraint; i++) {
	    	History history = new History();
	    	int currencyId = i - 1;
	    	history.setCurrencyname(asset.getData()[currencyId].getId());
	    	history.setInterval(lastInterval+INTERVAL_INCREASEMENT);
	    	history.setPriceusd(new BigDecimal((asset.getData()[currencyId].getPriceUsd())));
	    	hr.save(history);
	    	
	    }	   
		
	}
} 
