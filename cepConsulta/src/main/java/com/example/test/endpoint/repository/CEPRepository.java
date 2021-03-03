package com.example.test.endpoint.repository;

import com.example.test.endpoint.model.CEPEndereco;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CEPRepository extends JpaRepository<CEPEndereco, Long> {
    public CEPEndereco findByCep(String cep);
}