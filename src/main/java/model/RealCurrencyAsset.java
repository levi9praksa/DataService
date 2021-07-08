package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RealCurrencyAsset {
	
	RealCurrencyDTO[] data;
	String timestamp;	
}
