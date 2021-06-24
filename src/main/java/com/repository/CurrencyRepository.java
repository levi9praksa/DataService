package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

}
