package com.example.DataService;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import model.History;

@Service
@AllArgsConstructor
public class HistoryService {	
	final HistoryRepository hr;

	public List<History> historySearch(String name, int interval, int lastInterval){
		if(!name.isBlank() && interval >= 0) {	
			return hr.findAll().stream()
					.filter(lh -> lh.getCurrencyname().trim().equals(name) && lh.getInterval() >= lastInterval - interval)
					.collect(Collectors.toList());			
		}
		if(name.isBlank() && interval >= 0) {		
			return hr.findAll().stream()
					.filter(lh -> lh.getInterval() >= lastInterval - interval)
					.collect(Collectors.toList());			
		}
		if(!name.isBlank() && interval < 0) {
			return hr.findAll().stream()
					.filter(lh -> lh.getCurrencyname().trim().equals(name))
					.collect(Collectors.toList());			
		}
		return List.of();
	}
	
}
