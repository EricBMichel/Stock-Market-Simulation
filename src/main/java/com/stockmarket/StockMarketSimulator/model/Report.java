package com.stockmarket.StockMarketSimulator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	double highestCapital;
	double lowestCapital;
	int highestShares;
	int lowestShares;
	int highestCompanies;
	int lowestCompanies;
	long numberOfTransactions;
}
