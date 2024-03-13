package com.fiap.projeto.reporitory;

import com.fiap.projeto.model.Mensagem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class MensagemRepositoryIT {

    @Autowired
    private MensagemReporitory mensagemReporitory;

    @Test
    void devePermitirCriarTabela() {
        long totalRegistros = mensagemReporitory.count();
        assertThat(totalRegistros).isGreaterThan(0);
    }

    @Test
    void deveRegistrarMensagem() {
        var id = UUID.randomUUID();
        var mensagem = geraMensagem();
        mensagem.setId(id);

        var mensagemRecebida = registrarMensagem(mensagem);

        assertThat(mensagemRecebida).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemRecebida.getId()).isEqualTo(mensagem.getId());
    }

    @Test
    void deveListarMensagem() {
        var resultadosObtidos = mensagemReporitory.findAll();

        assertThat(resultadosObtidos).hasSizeGreaterThan(0);
    }

    @Test
    void deveBuscarMensagem() {
        var id = UUID.fromString("d7f82637-2cdb-4893-9e18-13d3eba94f0e");

        var mensagemRecebidaOptional = mensagemReporitory.findById(id);

        assertThat(mensagemRecebidaOptional).isPresent();

        mensagemRecebidaOptional.ifPresent( mensagemRecebida -> {
            assertThat(mensagemRecebida.getId()).isEqualTo(id);
        });
    }

    @Test
    void deveExcluirMensagem() {
        var id = UUID.fromString("3684f9f5-58c0-46ea-94a9-e61b0a8aca6a");

        mensagemReporitory.deleteById(id);

        var mensagemRecebida = mensagemReporitory.findById(id);

        assertThat(mensagemRecebida).isEmpty();
    }

    private Mensagem geraMensagem() {
        return Mensagem.builder()
                .usuario("NomeTeste").conteudo("teste").build();
    }

    private Mensagem registrarMensagem(Mensagem mensagem) {
        return mensagemReporitory.save(mensagem);
    }

}
