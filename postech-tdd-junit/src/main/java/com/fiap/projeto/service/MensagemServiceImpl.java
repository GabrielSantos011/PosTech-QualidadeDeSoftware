package com.fiap.projeto.service;

import com.fiap.projeto.model.Mensagem;
import com.fiap.projeto.reporitory.MensagemReporitory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemServiceImpl implements MensagemService {

    private final MensagemReporitory mensagemReporitory;

    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {
        mensagem.setId(UUID.randomUUID());
        return mensagemReporitory.save(mensagem);
    }

    @Override
    public Mensagem buscarMensagem(UUID id) {
        return mensagemReporitory.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mensagem não encontrada"));
    }

    @Override
    public Page<Mensagem> listarMensagens(Pageable pageable) {
        return mensagemReporitory.findAll(pageable);
    }

    @Override
    public Mensagem alterarMensagem(UUID id, Mensagem novaMensagem) {
        var mensagem = buscarMensagem(id);

        if (!mensagem.getId().equals(novaMensagem.getId()))
            throw new IllegalArgumentException("id não pode ser alterado");

        return mensagemReporitory.save(novaMensagem);
    }

    @Override
    public boolean removeMensagem(UUID id) {
        buscarMensagem(id);
        mensagemReporitory.deleteById(id);
        return true;
    }
}
