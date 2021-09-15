package com.claudiodornelles.desafio.repository;

import com.claudiodornelles.desafio.models.Salesman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman, String> {
}
