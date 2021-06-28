package com.example.DataService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.repository.CurrencyRepository;

import model.Currency;

@Service
public class Filters {
	
	private CurrencyRepository cr;
	
	public Filters(CurrencyRepository cr) {
		this.cr = cr;
	}
	
	public List<Currency> filterByRange(float minRange, float maxRange) {	
		return cr.findAll().stream()
				.filter(c -> c.getPriceusd() > minRange)
				.filter(c -> c.getPriceusd() < maxRange)
				.collect(Collectors.toList());
	}
	
	
	public List<Currency> filterTop3(String filter) {

		if (filter.equals("price")) {
			return cr.findAll().stream()
					.sorted(Comparator.comparingDouble(Currency::getPriceusd))
					.limit(3)
					.collect(Collectors.toList());
		}

		if (filter.equals("changepercent")) {
			return cr.findAll().stream()
					.sorted(Comparator.comparingDouble(Currency::getChangepercent24h))
					.limit(3)
					.collect(Collectors.toList());
		}

		return List.of();
	}
	
	
	public Currency searchByName(String name) {
		return cr.findAll().stream()
				.filter(c -> c.getName().trim().equals(name))
				.collect(Collectors.toList())
				.get(0);
	}
	
	public Currency searchBySymbol(String symbol) {
		return cr.findAll().stream()
				.filter(c -> c.getSymbol().trim().equals(symbol))
				.collect(Collectors.toList())
				.get(0);
	}
}
