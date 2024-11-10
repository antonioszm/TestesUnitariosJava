package com.example.demo.controller;

import com.example.demo.controller.TurmaController;
import com.example.demo.database.entities.Estudante;
import com.example.demo.service.EstudanteService;
import com.example.demo.service.TurmaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest(TurmaController.class)
class TurmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TurmaService turmaService;

    @MockBean
    private EstudanteService estudanteService;

    private Estudante estudante;

    @BeforeEach
    void setUp() {
        estudante = new Estudante();
        estudante.setId(1L);
        estudante.setNome("João Silva");
        estudante.setMatricula("123456");
    }

    @Test
    void adicionarEstudanteNaTurma() throws Exception {
        when(turmaService.adicionarEstudanteNaTurma(anyLong(), eq(estudante))).thenReturn(estudante);

        mockMvc.perform(MockMvcRequestBuilders.post("/turmas/{id}/estudantes", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(estudante)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(estudante.getId()))
                .andExpect(jsonPath("$.nome").value(estudante.getNome()))
                .andExpect(jsonPath("$.matricula").value(estudante.getMatricula()));

        verify(turmaService, times(1)).adicionarEstudanteNaTurma(eq(1L), eq(estudante));
    }

    @Test
    void removerEstudanteDaTurma() throws Exception {
        when(estudanteService.buscarEstudantePorId(anyLong())).thenReturn(estudante);
        doNothing().when(turmaService).removerEstudanteDaTurma(anyLong(), any(Estudante.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/turmas/{id}/estudantes/{estudanteId}", 1, 1))
                .andExpect(status().isOk());

        verify(estudanteService, times(1)).buscarEstudantePorId(eq(1L));
        verify(turmaService, times(1)).removerEstudanteDaTurma(eq(1L), eq(estudante));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
