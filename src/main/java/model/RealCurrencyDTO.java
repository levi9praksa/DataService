package model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RealCurrencyDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1391019066303622020L;
	private String id;
	private String symbol;
	private String currencySymbol;
	private String type;
	private String rateUsd;

	
}
