package com.fiap.projeto.reporitory;

import com.fiap.projeto.model.Mensagem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class MensagemRepositoryTest {

    @Mock
    private MensagemReporitory mensagemReporitory;

    private AutoCloseable openMock;

    @BeforeEach
    void setup() {
        //inicia todos os mocks da classe
        openMock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        //encerra os mocks
        openMock.close();
    }

    @Test
    void deveRegistrarMensagem() {
        //definição do objeto
        var mensagem = geraMensagem();

        //condição prévia
        when(mensagemReporitory.save(any(Mensagem.class))).thenReturn(mensagem);

        //chamada
        var mensagemRetornada = mensagemReporitory.save(mensagem);

        //validação
        assertThat(mensagemRetornada).isNotNull().isEqualTo(mensagem);

        //verificação se o método foi chamado
        verify(mensagemReporitory, times(1)).save(any(Mensagem.class));
        //fail("deve implementar");
    }

    @Test
    void deveAlterarMensagem() {
        var uuid = UUID.randomUUID();

        doNothing().when(mensagemReporitory).deleteById(any(UUID.class));

        mensagemReporitory.deleteById(uuid);

        verify(mensagemReporitory, times(1)).deleteById(any(UUID.class));

        //fail("deve implementar");
    }



    @Test
    void deveBuscarMensagem() {
        var uuid = UUID.randomUUID();
        var mensagem = geraMensagem();
        mensagem.setId(uuid);

        when(mensagemReporitory.findById(any(UUID.class))).thenReturn(Optional.of(mensagem));

        var mensagemRecebida = mensagemReporitory.findById(uuid);

        assertThat(mensagemRecebida).isPresent().containsSame(mensagem);

        mensagemRecebida.ifPresent(m->{
            assertThat(m.getId()).isEqualTo(mensagem.getId());
        });

        verify(mensagemReporitory, times(1)).findById(any(UUID.class));

        //fail("deve implementar");
    }

    @Test
    void deveListarMensagens() {
        var listaMensagem = Arrays.asList(geraMensagem(), geraMensagem(), geraMensagem());

        when(mensagemReporitory.findAll()).thenReturn(listaMensagem);

        var mensagensRecebidas = mensagemReporitory.findAll();

        assertThat(mensagensRecebidas).hasSize(3);

        verify(mensagemReporitory, times(1)).findAll();
    }

    private Mensagem geraMensagem() {
        return Mensagem.builder()
                .usuario("Gabriel").conteudo("teste").build();
    }

}
