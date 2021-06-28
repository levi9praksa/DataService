package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DataService.CurrencyService;
import com.example.DataService.Filters;

import model.Currency;

@RestController
public class CurrencyController {

	@Autowired
	private Filters filters;
	
	@Autowired
	private CurrencyService currencyService;
	
	@RequestMapping("/putCurrencies")
	public void putCurrencies(){
		currencyService.putCurrencies();
	}
	
	
	@RequestMapping(value = "/filterByRange", method = RequestMethod.GET)
	public List<Currency> filterByRange(@RequestParam(value = "min") float minRange, @RequestParam(value = "max") float maxRange) {
		return  filters.filterByRange(minRange, maxRange);

	}

	@RequestMapping(value = "/filterTop3", method = RequestMethod.GET)
	public List<Currency> filterTop3(@RequestParam(value = "filter") String filter) {
		return filters.filterTop3(filter);
	}

	@RequestMapping(value = "/searchByName", method = RequestMethod.GET)
	public Currency searchByName(@RequestParam(value = "name") String name) {
		return filters.searchByName(name);
	}

	@RequestMapping(value = "/searchBySymbol", method = RequestMethod.GET)
	public Currency searcgBySymbol(@RequestParam(value = "symbol") String symbol) {
		return filters.searchBySymbol(symbol);
	}
}
