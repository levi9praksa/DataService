package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.History;
@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

}
