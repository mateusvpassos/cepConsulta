package com.example.test.endpoint.controller;

import com.example.test.endpoint.exception.CEPInvalidoException;
import com.example.test.endpoint.exception.NotFoundException;
import com.example.test.endpoint.model.CEPEndereco;
import com.example.test.endpoint.service.CEPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/ceps")
public class CEPController {
    @Autowired
    private CEPService cepService;

    @GetMapping
    public ResponseEntity<?> cepListar(Pageable pageable){
        return new ResponseEntity<>(cepService.list(pageable), HttpStatus.OK);
    }

    @GetMapping("/{cep}")
    public CEPEndereco cepBuscar(@PathVariable String cep){
        try{
            return cepService.findByCep(cep);
        }catch (NotFoundException exc){
            System.err.println("CEP não encontrado!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado!", exc);
        }catch (CEPInvalidoException exc){
            System.err.println("CEP inválido!");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CEP inválido!", exc);
        }
    }

    @PostMapping
    public CEPEndereco cepSalvar(@RequestBody CEPEndereco cepEndereco){

        return cepService.save(cepEndereco);
    }

    @DeleteMapping("/{cep}")
    public CEPEndereco cepExcluir(@PathVariable String cep){
        try{
            return cepService.delete(cep);
        }catch (NotFoundException exc){
            System.err.println("CEP não encontrado!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado!", exc);
        }catch (CEPInvalidoException exc){
            System.err.println("CEP inválido!");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CEP inválido!", exc);
        }
    }

    @PutMapping
    public CEPEndereco cepEditar(@RequestBody CEPEndereco cepEndereco){

        return cepService.edit(cepEndereco);
    }
}