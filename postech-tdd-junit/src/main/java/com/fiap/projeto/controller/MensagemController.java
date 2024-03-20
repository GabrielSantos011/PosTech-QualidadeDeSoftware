package com.fiap.projeto.controller;

import com.fiap.projeto.model.Mensagem;
import com.fiap.projeto.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("mensagens")
@RequiredArgsConstructor
public class MensagemController {

    @Autowired
    private final MensagemService mensagemService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mensagem> registraMensagem(@RequestBody Mensagem mensagem) {
        var mensagemRecebida = mensagemService.registrarMensagem(mensagem);

        return new ResponseEntity<>(mensagemRecebida, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscaMensagem(@PathVariable String id) {
        try {
            var uuid = UUID.fromString(id);
            var mensagemRecebida = mensagemService.buscarMensagem(uuid);
            return new ResponseEntity<>(mensagemRecebida, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("id inválido", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/listar",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Mensagem>> listarMensagens(
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var mensagens = mensagemService.listarMensagens(pageable);

        return new ResponseEntity<>(mensagens, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> alterarMensagem(@PathVariable String id, @RequestBody Mensagem mensagem) {
        try {
            var uuid = UUID.fromString(id);
            var mensagemAtualizada = mensagemService.alterarMensagem(uuid, mensagem);
            return new ResponseEntity<>(mensagemAtualizada, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerMensagem(@PathVariable String id) {
        try {
            var uuid = UUID.fromString(id);
            mensagemService.removeMensagem(uuid);
            return new ResponseEntity<>("mensagem removida", HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
