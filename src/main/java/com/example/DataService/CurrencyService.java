package com.example.DataService;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import model.CurrencyDTO;
import model.History;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class CurrencyService {
	
	static final List<String> currencySymbolList = List.of("BTC", "ETH", "USDT", "BNB", "ADA", "DOGE", "XRP", "USDC", "DOT", "UNI" );
	
	@Value("${coin-api-url}")
	private String uri;
	
	final CurrencyRepository cr;
	final HistoryRepository hr;
	
	private static final int INTERVAL_INCREASEMENT = 1;
	private static final int STATUS_CODE_CONSTRAINT = 400;
	private static final int DEFAULT_THRESHOLD = 0;
	private static final int FIRST_CURRENCY = 1;
		
	public Asset putCurrencies() {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
		
		if (response.getStatusCodeValue() < STATUS_CODE_CONSTRAINT) {
			
			try {
				Asset result = restTemplate.getForObject(uri, Asset.class);
	
				for (int i = FIRST_CURRENCY; i <= result.getData().length - 1; i++) {
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
				
			}catch(Exception e) {
				return null;
			}
			
		} else {			
			return null;
			
		}
	}
	
	public int getLastHistoryInterval() {
		
		if(hr.findAll().isEmpty()) {
			return 0;
		}
		
		return hr.findAll()
				.stream()
				.skip(hr.findAll().stream().count() - 1)
				.findFirst()
				.get()
				.getInterval();
	}
	
	
	@Scheduled(cron = "${history-update-interval}")
	public void updateHistory(){
		
		Asset asset = putCurrencies();
		
    	List<CurrencyDTO> currencyList = Arrays.asList(asset.getData())
    			.stream()
    			.filter(cl -> currencySymbolList.stream()
    		               .anyMatch(csl -> csl.equalsIgnoreCase(cl.getSymbol())))
    			.collect(Collectors.toList());
		
		int lastInterval = getLastHistoryInterval();
		for(int i = FIRST_CURRENCY; i <= currencyList.size(); i++) {
	    	History history = new History();
	    	int currencyId = i - 1;
	    	history.setCurrencyname(currencyList.get(currencyId).getId());
	    	history.setInterval(lastInterval+INTERVAL_INCREASEMENT);
	    	history.setPriceusd(new BigDecimal((currencyList.get(currencyId).getPriceUsd())));
	    	hr.save(history);
	    	
	    }	   
		
	}
} 
