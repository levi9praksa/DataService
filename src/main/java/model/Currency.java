package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.Data;


/**
 * The persistent class for the currency database table.
 * 
 */
@Entity
@NamedQuery(name="Currency.findAll", query="SELECT c FROM Currency c")
@Data 
public class Currency implements Serializable {
	@Override
	public String toString() {
		return "Currency [id=" + id  + ", name=" + name + ", symbol=" + symbol + ", priceusd=" + priceusd 
				+ ", changepercent24h=" + changepercent24h  + ", threshold=" + threshold + "]";
	}
 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private String symbol;
	
	private BigDecimal priceusd;
	
	private BigDecimal changepercent24h;
	
	private BigDecimal threshold;

}