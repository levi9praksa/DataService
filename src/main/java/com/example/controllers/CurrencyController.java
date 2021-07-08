package com.example.controllers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DataService.CurrencyService;
import com.example.DataService.Filters;

import lombok.AllArgsConstructor;
import model.Currency;

@AllArgsConstructor
@RestController
public class CurrencyController {

	private Filters filters;
	private CurrencyService currencyService;
	
	final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/currencies", method = RequestMethod.GET) 
	public Collection<Currency> getCurrencies() {
		return currencyService.getAllCurrencies();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/putThreshold", method = RequestMethod.PUT) 
	public ResponseEntity<?> putCurrencyThreshold(@RequestBody Currency currency) {
		return currencyService.updateThreshold(currency);
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping("/putCurrencies")
	public void putCurrencies(){
		currencyService.putCurrencies();
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/filterByRange", method = RequestMethod.GET)
	public List<Currency> filterByRange(@RequestParam(value = "min") float minRange, @RequestParam(value = "max") float maxRange) {
		return  filters.filterByRange(minRange, maxRange);

	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/filterTop3", method = RequestMethod.GET)
	public List<Currency> filterTop3(@RequestParam(value = "filter") String filter) {
		return filters.filterTop3(filter);
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/searchByName", method = RequestMethod.GET)
	public Currency searchByName(@RequestParam(value = "name") String name) {
		return filters.searchByName(name);
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/searchBySymbol", method = RequestMethod.GET)
	public Currency searcgBySymbol(@RequestParam(value = "symbol") String symbol) {
		return filters.searchBySymbol(symbol);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/currencyExchange")
	public BigDecimal currencyExchange(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		return currencyService.currencyExchange(filters.searchByName(from), filters.searchByName(to));

	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/currencyExchangeIntoRealCurrency")
	public BigDecimal currencyExchangeIntoRealCurrency(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		return currencyService.currencyExchangeIntoRealCurrency(filters.searchByName(from), to);

	}
}
