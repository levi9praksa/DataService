package model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CurrencyDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7196689619928326856L;
	private String id;
	private String rank;
	private String symbol;
	private String name;
	private String supply;
	private String maxSupply;
	private String marketCapUsd;
	private String volumeUsd24Hr;
	private String priceUsd;
	private String changePercent24Hr;
	private String vwap24Hr;
	private String explorer;
		
	

}
