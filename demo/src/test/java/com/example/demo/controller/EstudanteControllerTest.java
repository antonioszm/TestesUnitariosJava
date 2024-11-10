package com.example.demo.controller;

import org.junit.jupiter.api.Test;

import com.example.demo.database.entities.Estudante;
import com.example.demo.service.EstudanteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstudanteController.class)
class EstudanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudanteService estudanteService;

    private ObjectMapper objectMapper;

    private Estudante estudante;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        estudante = new Estudante();
        estudante.setId(1L);
        estudante.setNome("João Silva");
        estudante.setMatricula("123456");
    }

    @Test
    void listarEstudantes() throws Exception {
        List<Estudante> listaEstudantes = Arrays.asList(estudante);
        when(estudanteService.listarEstudantes()).thenReturn(listaEstudantes);

        mockMvc.perform(MockMvcRequestBuilders.get("/estudantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(estudante.getId()))
                .andExpect(jsonPath("$[0].nome").value(estudante.getNome()))
                .andExpect(jsonPath("$[0].matricula").value(estudante.getMatricula()));
    }

    @Test
    void buscarEstudantePorId() throws Exception {
        when(estudanteService.buscarEstudantePorId(1L)).thenReturn(estudante);

        mockMvc.perform(MockMvcRequestBuilders.get("/estudantes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(estudante.getId()))
                .andExpect(jsonPath("$.nome").value(estudante.getNome()))
                .andExpect(jsonPath("$.matricula").value(estudante.getMatricula()));
    }

    @Test
    void cadastrarEstudante() throws Exception {
        Estudante novoEstudante = new Estudante();
        novoEstudante.setNome("Maria Santos");
        novoEstudante.setMatricula("789012");

        when(estudanteService.cadastrarEstudante(novoEstudante.getNome(), novoEstudante.getMatricula())).thenReturn(novoEstudante);

        String jsonContent = objectMapper.writeValueAsString(novoEstudante);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(novoEstudante.getNome()))
                .andExpect(jsonPath("$.matricula").value(novoEstudante.getMatricula()));
    }

    @Test
    void atualizarEstudante() throws Exception{
        Estudante estudanteAtualizado = new Estudante();
        estudanteAtualizado.setId(1L);
        estudanteAtualizado.setNome("João Santos");
        estudanteAtualizado.setMatricula("987654");

        when(estudanteService.atualizarEstudante(estudante.getId(), estudanteAtualizado.getNome(), estudanteAtualizado.getMatricula())).thenReturn(estudanteAtualizado);

        String jsonContent = objectMapper.writeValueAsString(estudanteAtualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(estudanteAtualizado.getNome()))
                .andExpect(jsonPath("$.matricula").value(estudanteAtualizado.getMatricula()));
    }

    @Test
    void removerEstudante() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/estudantes/{id}", 1))
                .andExpect(status().isNoContent());
    }
}