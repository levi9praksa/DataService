package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Asset {
	
	CurrencyDTO[] data;
	String timestamp;
	
}
