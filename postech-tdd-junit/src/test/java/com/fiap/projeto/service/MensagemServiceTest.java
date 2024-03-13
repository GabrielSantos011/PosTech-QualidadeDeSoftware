package com.fiap.projeto.service;

import com.fiap.projeto.model.Mensagem;
import com.fiap.projeto.reporitory.MensagemReporitory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MensagemServiceTest {

    @Mock
    private MensagemReporitory mensagemReporitory;

    private MensagemService mensagemService;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        //inicia todos os mocks da classe
        mock = MockitoAnnotations.openMocks(this);
        mensagemService = new MensagemServiceImpl(mensagemReporitory);
    }

    @AfterEach
    void tearDown() throws Exception {
        //encerra os mocks
        mock.close();
    }

    @Test
    void devePermitirRegistrarMensagem() {
//        fail("implementar teste");
        var mensagem = geraMensagem();

        when(mensagemReporitory.save(any(Mensagem.class)))
                .thenAnswer(i -> i.getArgument(0));

        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        assertThat(mensagemRegistrada).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemRegistrada.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagemRegistrada.getId()).isEqualTo(mensagem.getId());
        assertThat(mensagemRegistrada.getUsuario()).isEqualTo(mensagem.getUsuario());

        verify(mensagemReporitory, times(1)).save(any(Mensagem.class));
    }

    @Test
    void devePermitirBuscarMensagem() {
        //fail("implementar teste");
        var mensagem = geraMensagem();
        var id = UUID.randomUUID();
        mensagem.setId(id);

        when(mensagemReporitory.findById(any(UUID.class))).thenReturn(Optional.of(mensagem));

        var mensagemRecebida = mensagemService.buscarMensagem(id);

        assertThat(mensagemRecebida).isEqualTo(mensagem);
        verify(mensagemReporitory, times(1)).findById(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_BuscarMensagemInexistente() {
        var id = UUID.randomUUID();

        when(mensagemReporitory.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemService.buscarMensagem(id)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem não encontrada");

        verify(mensagemReporitory, times(1)).findById(any(UUID.class));
    }

    @Test
    void devePermitirAlterarMensagem() {
        //fail("implementar teste");
        var mensagem = geraMensagem();
        var id = UUID.randomUUID();
        mensagem.setId(id);

        var mensagemNova = new Mensagem();
        mensagemNova.setId(mensagem.getId());
        mensagemNova.setUsuario(mensagem.getUsuario());
        mensagemNova.setConteudo("abcabcabcabc");

        when(mensagemReporitory.findById(id)).thenReturn(Optional.of(mensagem));

        when(mensagemReporitory.save(mensagemNova)).thenAnswer(i -> i.getArgument(0));

        var mensagemRecebida = mensagemService.alterarMensagem(id, mensagemNova);

        assertThat(mensagemRecebida).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemRecebida).isNotEqualTo(mensagem);
        assertThat(mensagemRecebida.getId()).isEqualTo(mensagemNova.getId());
        assertThat(mensagemRecebida.getUsuario()).isEqualTo(mensagemNova.getUsuario());
        assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagemNova.getConteudo());

        verify(mensagemReporitory, times(1)).findById(any(UUID.class));
        verify(mensagemReporitory, times(1)).save(any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_alterarMensagemInexistente() {
        var id = UUID.randomUUID();
        var mensagem = geraMensagem();
        mensagem.setId(id);

        when(mensagemReporitory.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagem)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem não encontrada");

        verify(mensagemReporitory, times(1)).findById(any(UUID.class));
        verify(mensagemReporitory, never()).save(any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_alterarMensagemIdDiferente() {
        var mensagem = geraMensagem();
        var id = UUID.randomUUID();
        mensagem.setId(id);

        var mensagemNova = new Mensagem();
        mensagemNova.setId(UUID.randomUUID());
        mensagemNova.setUsuario(mensagem.getUsuario());
        mensagemNova.setConteudo("abcabcabcabc");

        when(mensagemReporitory.findById(id)).thenReturn(Optional.of(mensagem));

        assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagemNova)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id não pode ser alterado");

        verify(mensagemReporitory, times(1)).findById(any(UUID.class));
        verify(mensagemReporitory, never()).save(any(Mensagem.class));
    }

    @Test
    void devePermitirRemoverMensagem() {
        //fail("implementar teste");
        var id = UUID.randomUUID();
        var mensagem = geraMensagem();
        mensagem.setId(id);

        when(mensagemReporitory.findById(id))
                .thenReturn(Optional.of(mensagem));

        doNothing().when(mensagemReporitory).deleteById(id);
        var deletado = mensagemService.removeMensagem(id);

        assertThat(deletado).isTrue();

        verify(mensagemReporitory, times(1)).findById(any(UUID.class));
        verify(mensagemReporitory, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_removeIdInexistente() {
        var id = UUID.randomUUID();

        when(mensagemReporitory.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemService.removeMensagem(id)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mensagem não encontrada");

        verify(mensagemReporitory, times(1)).findById(any(UUID.class));
        verify(mensagemReporitory, never()).deleteById(any(UUID.class));
    }
    @Test
    void devePermitirListaMensagens() {
        //fail("implementar teste");
        Page<Mensagem> listaMensagens = new PageImpl<>(Arrays.asList(
                geraMensagem(),
                geraMensagem()
        ));

        when(mensagemReporitory.findAll(any(Pageable.class))).thenReturn(listaMensagens);

        var listaObtida = mensagemService.listarMensagens(Pageable.unpaged());

        assertThat(listaObtida).hasSize(2);
        assertThat(listaObtida.getContent()).asList().allSatisfy(mensagem -> {
            assertThat(mensagem).isNotNull().isInstanceOf(Mensagem.class);
        });

        verify(mensagemReporitory, times(1)).findAll(any(Pageable.class));
    }

    private Mensagem geraMensagem() {
        return Mensagem.builder()
                .usuario("Gabriel").conteudo("teste").build();
    }

}
