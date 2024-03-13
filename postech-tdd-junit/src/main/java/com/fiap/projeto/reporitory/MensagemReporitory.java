package com.fiap.projeto.reporitory;

import com.fiap.projeto.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MensagemReporitory extends JpaRepository<Mensagem, UUID> {
}
