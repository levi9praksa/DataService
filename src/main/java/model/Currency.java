package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the currency database table.
 * 
 */
@Entity
@NamedQuery(name="Currency.findAll", query="SELECT c FROM Currency c")
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

	public Currency() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getChangepercent24h() {
		return this.changepercent24h;
	}

	public void setChangepercent24h(float changepercent24h) {
		this.changepercent24h = changepercent24h;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPriceusd() {
		return this.priceusd;
	}

	public void setPriceusd(float priceusd) {
		this.priceusd = priceusd;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public float getThreshold() {
		return this.threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

}