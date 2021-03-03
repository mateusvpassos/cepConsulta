package com.example.test.endpoint.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.test.endpoint.exception.CEPInvalidoException;
import com.example.test.endpoint.exception.NotFoundException;
import com.example.test.endpoint.model.CEPEndereco;
import com.example.test.endpoint.repository.CEPRepository;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CEPService {

    @Autowired
    private CEPRepository cepRepository;

    public CEPEndereco findByCep(String cep) {
        if (cep.length() > 8) {
            throw new CEPInvalidoException();
        }
        String cepHifen = cep.substring(0, 5) + "-" + cep.substring(5);
        CEPEndereco cepEndereco = cepRepository.findByCep(cepHifen);

        if (cepEndereco == null) {
            System.out.println("---------------------");
            System.out.println("Buscando CEP no ViaCEP: " + cepHifen);
            cepEndereco = buscaViaCep(cep);
            if (cepEndereco.getLocalidade() == null) {
                System.out.println("---------------------");
                System.out.println("Buscando CEP no RepublicaVirtual: " + cepHifen);
                cepEndereco = buscaRepublicaVirtual(cep);
                if (cepEndereco.getLocalidade() == null) {
                    throw new NotFoundException();
                }else{
                    System.out.println("Encontrado no RepublicaVirtual: " + cepHifen);
                }
            }else{
                System.out.println("Encontrado no ViaCEP: " + cepHifen);
            }
            cepRepository.save(cepEndereco);
        }else{
            System.out.println("---------------------");
            System.out.println("Encontrado no banco: " + cepHifen);
        }

        return cepEndereco;
    }

    public ResponseEntity<?> list(Pageable pageable) {
        return new ResponseEntity<>(cepRepository.findAll(pageable), HttpStatus.OK);
    }

    public CEPEndereco save(CEPEndereco cepEndereco) {
        return cepRepository.save(cepEndereco);
    }

    public CEPEndereco delete(String cep) {
        if (cep.length() > 8) {
            throw new CEPInvalidoException();
        }
        String cepHifen = cep.substring(0, 5) + "-" + cep.substring(5);
        CEPEndereco cepEndereco = cepRepository.findByCep(cepHifen);
        if (cepEndereco == null) {
            throw new NotFoundException();
        }
        cepRepository.delete(cepEndereco);
        return cepEndereco;

    }

    public CEPEndereco edit(CEPEndereco cepEndereco) {

        return cepRepository.save(cepEndereco);
    }

    // ------------------------------MÉTODO QUE BUSCA CEP NO
    // VIACEP---------------------------------//
    public CEPEndereco buscaViaCep(String cep) {
        String ur = "http://viacep.com.br/ws/";
        String req = ur + cep + "/json/unicode";

        try {
            URL url = new URL(req);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            if (conexao.getResponseCode() != 200)
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

            BufferedReader buffereReader = new BufferedReader(new InputStreamReader((conexao.getInputStream())));

            String resposta, jsonEmString = "";
            while ((resposta = buffereReader.readLine()) != null) {
                jsonEmString += resposta;
            }

            Gson gson = new Gson();
            CEPEndereco CEP = gson.fromJson(jsonEmString, CEPEndereco.class);

            return CEP;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ------------------------------MÉTODO QUE BUSCA CEP NO
    // REPUBLICAVIRTUAL---------------------------------//
    public CEPEndereco buscaRepublicaVirtual(String cep) {
        String ur = "http://cep.republicavirtual.com.br/web_cep.php?cep=";
        String req = ur + cep + "&formato=json";

        try {
            URL url = new URL(req);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            if (conexao.getResponseCode() != 200)
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

            BufferedReader buffereReader = new BufferedReader(new InputStreamReader((conexao.getInputStream())));

            String resposta, jsonEmString = "";
            while ((resposta = buffereReader.readLine()) != null) {
                jsonEmString += resposta;
            }

            Gson gson = new Gson();
            EnderecoRepVirtual enderecoRepVirtual = gson.fromJson(jsonEmString, EnderecoRepVirtual.class);
            CEPEndereco CEP = new CEPEndereco();
            CEP.setBairro(enderecoRepVirtual.bairro);
            CEP.setUf(enderecoRepVirtual.uf);
            CEP.setCep(cep);
            CEP.setLocalidade(enderecoRepVirtual.cidade);
            CEP.setLogradouro(enderecoRepVirtual.tipo_logradouro + " " + enderecoRepVirtual.logradouro);
            return CEP;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // CLASSE DE MODELO PARA BUSCA NO REPUBLICA VIRTUAL//
    public class EnderecoRepVirtual {
        private String uf;
        private String cidade;
        private String bairro;
        private String tipo_logradouro;
        private String logradouro;
    }
}
