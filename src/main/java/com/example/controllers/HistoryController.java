package com.example.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DataService.CurrencyService;
import com.example.DataService.HistoryService;

import lombok.AllArgsConstructor;
import model.History;

@RestController
@AllArgsConstructor
public class HistoryController {
	
	private HistoryService historyService;
	private CurrencyService currencyService;
	
	@RequestMapping(value="/historySearch", method = RequestMethod.GET)
	public List<History> historySearch(@RequestParam(value = "name") String name, @RequestParam(value="interval") int interval){
		return historyService.historySearch(name, interval, currencyService.getLastHistoryInterval());
	}
	
}
