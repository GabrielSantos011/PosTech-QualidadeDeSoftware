package com.fiap.projeto.service;

import com.fiap.projeto.model.Mensagem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class MensagemServiceIT {

    @Autowired
    private MensagemService mensagemService;

    @Test
    void devePermitirRegistrarMensagem() {
        var mensagem = geraMensagem();

        var mensagemObtida = mensagemService.registrarMensagem(mensagem);

        assertThat(mensagemObtida).isNotNull().isInstanceOf(Mensagem.class);
        assertThat(mensagemObtida.getId()).isNotNull();
        assertThat(mensagemObtida.getDataCriacao()).isNotNull();
        assertThat(mensagemObtida.getGostei()).isEqualTo(0);
    }

    @Test
    void devePermitirBuscarMensagem() {
        var id = UUID.fromString("a15cc825-8a70-4846-96b1-ba6791b5cb8b");

        var mensagemObtida = mensagemService.buscarMensagem(id);

        assertThat(mensagemObtida).isNotNull().isInstanceOf(Mensagem.class);
        assertThat(mensagemObtida.getId()).isEqualTo(id);
    }

    @Test
    void deveGerarExcecao_BuscarMensagemInexistente() {
        var id = UUID.randomUUID();

        assertThatThrownBy(() -> mensagemService.buscarMensagem(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem não encontrada");
    }

    @Test
    void devePermitirAlterarMensagem() {
        var id = UUID.fromString("a15cc825-8a70-4846-96b1-ba6791b5cb8b");

        var mensagem = geraMensagem();
        mensagem.setId(id);

        var mensagemObtida = mensagemService.alterarMensagem(id, mensagem);

        assertThat(mensagemObtida.getId()).isEqualTo(id);
        assertThat(mensagemObtida.getConteudo()).isEqualTo(mensagem.getConteudo());
    }

    @Test
    void deveGerarExcecao_alterarMensagemInexistente() {
        var id = UUID.fromString("aaacc825-8a70-4846-96b1-ba6791b5caaa");

        var mensagem = geraMensagem();
        mensagem.setId(id);

        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem não encontrada");
    }

    @Test
    void deveGerarExcecao_alterarMensagemIdDiferente() {
        var id = UUID.fromString("a15cc825-8a70-4846-96b1-ba6791b5cb8b");

        var mensagem = geraMensagem();
        mensagem.setId(UUID.randomUUID());

        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id não pode ser alterado");
    }

    @Test
    void devePermitirRemoverMensagem() {
        var id = UUID.fromString("d7f82637-2cdb-4893-9e18-13d3eba94f0e");

        var resultadoObtido = mensagemService.removeMensagem(id);

        assertThat(resultadoObtido).isTrue();
    }

    @Test
    void deveGerarExcecao_removeIdInexistente() {
        var id = UUID.fromString("aaaaa637-2cdb-aaaa-9e18-13d3eba94f0e");

        assertThatThrownBy(() -> mensagemService.removeMensagem(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem não encontrada");
    }

    @Test
    void devePermitirListaMensagens() {
        Page<Mensagem> listaMensagem = mensagemService.listarMensagens(Pageable.unpaged());

        assertThat(listaMensagem).hasSize(3);
        assertThat(listaMensagem.getContent()).asList().allSatisfy(mensagemObtida -> {
            assertThat(mensagemObtida).isNotNull();
        });
    }

    private Mensagem geraMensagem() {
        return Mensagem.builder()
                .usuario("Gabriel").conteudo("teste").build();
    }

}
