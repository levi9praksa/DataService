package model;

import java.io.Serializable;

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
		return "Currency [id=" + id + ", changepercent24h=" + changepercent24h + ", name=" + name + ", priceusd="
				+ priceusd + ", symbol=" + symbol + ", threshold=" + threshold + "]";
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private float changepercent24h;
	
	private String name;

	private float priceusd;

	private String symbol;

	private float threshold;
	


}