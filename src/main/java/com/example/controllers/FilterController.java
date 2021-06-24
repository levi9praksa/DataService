package com.example.controllers;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.CurrencyRepository;

import model.Currency;

@RequestMapping("/filter")
@RestController
public class FilterController {
	
	//TODO tek sam poceo sa filterima, nema potrebe za gledanjem
	
	@Autowired
	CurrencyRepository cr;
	
	
	@RequestMapping("/filterByRange")
	public List<Currency> filterByRange() {
		
		//will take attributes from session, but for now we will just initialize them for testing
		float minRange = 15;
		float maxRange = 20000;
		
		List<Currency> allCurrencies = cr.findAll();
		List<Currency> filteredList = new ArrayList<Currency>();
		
		for(Currency c : allCurrencies) {
			
			if(c.getPriceusd() > minRange && c.getPriceusd() < maxRange) {
				filteredList.add(c);
				System.out.println(c.getId() + "\t" + c.getName() + "\t" + c.getSymbol() + "\t"  + c.getPriceusd() + "\t" + c.getChangepercent24h() + "\t" + c.getThreshold());
				
			}
		}
		
		return filteredList;
		
		
	}
}
