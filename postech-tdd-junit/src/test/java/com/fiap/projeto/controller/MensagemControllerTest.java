package com.fiap.projeto.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.projeto.model.Mensagem;
import com.fiap.projeto.service.MensagemService;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MensagemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MensagemService mensagemService;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        //inicia todos os mocks da classe
        mock = MockitoAnnotations.openMocks(this);
        MensagemController mensagemController = new MensagemController(mensagemService);
        mockMvc = MockMvcBuilders.standaloneSetup(mensagemController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        //encerra os mocks
        mock.close();
    }

    @Nested
    class RegistrarMensagem {

        @Test
        void deveRegistrarMensagem() throws Exception {
            var mensagem = geraMensagem();

            when(mensagemService.registrarMensagem(any(Mensagem.class)))
                    .thenAnswer(i -> i.getArgument(0));

            mockMvc.perform(
                    post("/mensagens")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            asJsonString(mensagem))
                    ).andExpect(status().isCreated());

            verify(mensagemService, times(1))
                    .registrarMensagem(any(Mensagem.class));
        }

    }

    @Nested
    class BuscarMensagem {

        @Test
        void deveBuscarMensagem() throws Exception {
            var mensagem = geraMensagem();
            var id = UUID.randomUUID();
            mensagem.setId(id);

            when(mensagemService.buscarMensagem(any(UUID.class))).thenReturn(mensagem);

            mockMvc.perform(get("/mensagens/{id}", id))
                    .andExpect(status().isOk());

            verify(mensagemService, times(1)).buscarMensagem(any(UUID.class));
        }

        @Test
        void deveGerarExeção_buscarMensagem_idNaoExiste() throws Exception {
            var id = UUID.randomUUID();

            when(mensagemService.buscarMensagem(id))
                    .thenThrow(IllegalArgumentException.class);

            mockMvc.perform(get("/mensagens/{id}", id))
                    .andExpect(status().isBadRequest());

            verify(mensagemService, times(1)).buscarMensagem(any(UUID.class));
        }

    }

    @Nested
    class AlterarMensagem {

        @Test
        void deveAlterarMensagem() throws Exception {
            var id = UUID.randomUUID();
            var mensagem = geraMensagem();
            mensagem.setId(id);

            when(mensagemService.alterarMensagem(id, mensagem))
                    .thenAnswer(i->i.getArgument(1));

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(mensagem)))
                    .andExpect(status().isAccepted());

            verify(mensagemService, times(1)).alterarMensagem(id, mensagem);
        }

        @Test
        void deveGerarExcecao_alterarMensagem_idNaoExiste() throws Exception {
            var id = UUID.randomUUID();
            var mensagem = geraMensagem();
            mensagem.setId(id);

            when(mensagemService.alterarMensagem(id, mensagem)).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(put("/mensagens/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(mensagem)))
                    .andExpect(status().isBadRequest());

            verify(mensagemService, times(1)).alterarMensagem(id, mensagem);
        }

        @Test
        void deveGerarExcecao_alterarMensagem_idDiferente() throws Exception {
            var id = UUID.randomUUID();
            var mensagem = geraMensagem();
            mensagem.setId(UUID.randomUUID());

            when(mensagemService.alterarMensagem(id, mensagem)).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
                    .andExpect(status().isBadRequest());
                    //.andExpect(content().string("mensagem da exceção"));

            verify(mensagemService, times(1)).alterarMensagem(id, mensagem);
        }

    }

    @Nested
    class RemoverMensagem {

        @Test
        void deveRemoverMensagem() throws Exception {
            var id = UUID.randomUUID();

            when(mensagemService.removeMensagem(id)).thenReturn(true);

            mockMvc.perform(delete("/mensagens/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().string("mensagem removida"));

            verify(mensagemService, times(1)).removeMensagem(any(UUID.class));
        }

        @Test
        void deveGerarExcecao_removerMensagem_idNaoExiste() throws Exception {
            var id = UUID.randomUUID();
            var mensagemErro = "mensagem não encontrada";

            when(mensagemService.removeMensagem(id)).thenThrow(new IllegalArgumentException(mensagemErro));

            mockMvc.perform(delete("/mensagens/{id}", id))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(mensagemErro));

            verify(mensagemService, times(1)).removeMensagem(any(UUID.class));
        }

    }

    @Nested
    class ListarMensagem {

        @Test
        void deveListarMensagem() throws Exception {
            var mensagem = geraMensagem();
            Page<Mensagem> listaMensagens = new PageImpl<>(Arrays.asList(
                    geraMensagem(),
                    geraMensagem()
            ));

            when(mensagemService.listarMensagens(any(Pageable.class))).thenReturn(listaMensagens);

            mockMvc.perform(get("/mensagens/listar")
                    .param("page", "0")
                    .param("size", "10")
                            .characterEncoding("UTF-8")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", not(empty())))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }

    }

    private Mensagem geraMensagem() {
        return Mensagem.builder()
                .usuario("Gabriel").conteudo("teste").build();
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
