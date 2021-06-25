package com.example.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.CurrencyRepository;

import model.Currency;

@RequestMapping("/filter")
@RestController
public class FilterController {

	@Autowired
	CurrencyRepository cr;

	// Just for testing
	public void printCurrency(Currency c) {
		System.out.println(c.getName().trim() + "\t" + c.getSymbol().trim() + "\t" + c.getPriceusd() + "\t"
				+ c.getChangepercent24h() + "\t" + c.getThreshold());
	}

	@RequestMapping(value = "/filterByRange", method = RequestMethod.GET)
	public List<Currency> filterByRange(@RequestParam(value = "min") float minRange,
			@RequestParam(value = "max") float maxRange) {

		List<Currency> allCurrencies = cr.findAll();
		List<Currency> filteredList = new ArrayList<Currency>();

		for (Currency c : allCurrencies) {

			if (c.getPriceusd() > minRange && c.getPriceusd() < maxRange) {
				filteredList.add(c);
				printCurrency(c);

			}
		}

		return filteredList;

	}

	@RequestMapping(value = "/filterTop3", method = RequestMethod.GET)
	public List<Currency> filterTop3(@RequestParam(value = "filter") String filter,
			@RequestParam(value = "method") String m) {

		List<Currency> allCurrencies = cr.findAll();
		List<Currency> filteredList = new ArrayList<Currency>();

		if (filter.equals("price")) {
			Collections.sort(allCurrencies, new Comparator<Currency>() {
				@Override
				public int compare(Currency c1, Currency c2) {
					return (int) (c2.getPriceusd() - c1.getPriceusd());
				}
			});

		}

		else if (filter.equals("changepercent")) {
			Collections.sort(allCurrencies, new Comparator<Currency>() {
				@Override
				public int compare(Currency c1, Currency c2) {
					return (int) (c2.getChangepercent24h() - c1.getChangepercent24h());
				}
			});
		}

		else {
			return null;
		}

		if (m.equals("low")) {
			filteredList = allCurrencies.subList(allCurrencies.size() - 3, allCurrencies.size());
		} else {
			filteredList = allCurrencies.subList(0, 3);
		}

		for (Currency c : filteredList) {
			printCurrency(c);
		}
		return filteredList;
	}

	@RequestMapping(value = "/searchByName", method = RequestMethod.GET)
	public Currency searchByName(@RequestParam(value = "name") String name) {

		List<Currency> allCurrencies = cr.findAll();

		for (Currency c : allCurrencies) {
			if (c.getName().trim().equals(name)) {
				printCurrency(c);
				return c;
			}
		}

		return null;
	}

	@RequestMapping(value = "/searchBySymbol", method = RequestMethod.GET)
	public Currency searcgBySymbol(@RequestParam(value = "symbol") String symbol) {

		List<Currency> allCurrencies = cr.findAll();

		for (Currency c : allCurrencies) {
			if (c.getSymbol().trim().equals(symbol)) {
				printCurrency(c);
				return c;
			}
		}

		return null;
	}

}
