package com.example.test.endpoint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "ceps")
public class CEPEndereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 12)
    private String cep;

    @Column(length = 150)
    private String logradouro;

    @Column(length = 150)
    private String complemento;

    @Column(length = 150)
    private String bairro;

    @Column(length = 150)
    private String localidade;

    @Column(length = 2)
    private String uf;

    @Column(length = 150)
    private String tipo_logradouro;

}
