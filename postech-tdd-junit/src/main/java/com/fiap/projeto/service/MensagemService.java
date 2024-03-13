package com.fiap.projeto.service;

import com.fiap.projeto.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MensagemService {

    Mensagem registrarMensagem(Mensagem mensagem);
    Mensagem buscarMensagem(UUID id);

    Page<Mensagem> listarMensagens(Pageable pageable);
    Mensagem alterarMensagem(UUID id, Mensagem novaMensagem);
    boolean removeMensagem(UUID id);

}
