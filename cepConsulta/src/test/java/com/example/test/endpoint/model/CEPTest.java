package com.example.test.endpoint.model;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CEPTest {
    //@Test
    public void test(){
        CEPEndereco cep = new CEPEndereco();
        cep.setBairro("Bairro");
        cep.setCep("00000-000");
        cep.setComplemento("Complemento");
        cep.setLocalidade("Cidade");
        cep.setLogradouro("Logradouro");
        cep.setTipo_logradouro("Rua");
        cep.setUf("UF");
        System.out.println(cep.toString());
    }
}
