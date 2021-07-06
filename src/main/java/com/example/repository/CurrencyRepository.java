package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Currency;
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

}
