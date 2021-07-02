package com.example.DataService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.repository.CurrencyRepository;

import lombok.AllArgsConstructor;
import model.Currency;

@AllArgsConstructor
@Service
public class Filters {
	
	private static final String CHANGEPERCENT24H_FILTER = "changepercent";
	private static final String PRICE_FILTER = "price";
	final CurrencyRepository cr;
		
	public List<Currency> filterByRange(float minRange, float maxRange) {			
		return cr.findAll().stream()
				.filter(c -> c.getPriceusd().floatValue() > minRange && c.getPriceusd().floatValue() < maxRange)
				.collect(Collectors.toList());
			
	}
	
	
	public List<Currency> filterTop3(String filter) {

		if (filter.equals(PRICE_FILTER)) {
			return cr.findAll().stream()
					.sorted((c1, c2) -> c2.getPriceusd().compareTo(c1.getPriceusd()))
					.limit(3)
					.collect(Collectors.toList());
		}

		if (filter.equals(CHANGEPERCENT24H_FILTER)) {
			return cr.findAll().stream()
					.sorted((c1, c2) -> c2.getChangepercent24h().compareTo(c1.getChangepercent24h()))
					.limit(3)
					.collect(Collectors.toList());
		}

		return List.of();
	}
	
	
	public Currency searchByName(String name) {
		
		if(cr.findAll().isEmpty()) {
			return null;
		}
		
		return cr.findAll().stream()
				.filter(c -> c.getName().trim().equals(name))
				.findFirst()
				.get();
				
	}
	
	public Currency searchBySymbol(String symbol) {
		
		if(cr.findAll().isEmpty()) {
			return null;
		}
		
		return cr.findAll().stream()
				.filter(c -> c.getSymbol().trim().equals(symbol))
				.findFirst()
				.get();
	}
	
	
}
