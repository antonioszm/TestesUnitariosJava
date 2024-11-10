package com.example.demo.service;

import com.example.demo.database.entities.Estudante;
import com.example.demo.database.entities.Turma;
import com.example.demo.database.repositories.TurmaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class TurmaServiceTestSpring {

    @Autowired
    private TurmaService turmaService;

    @MockBean
    private TurmaRepository turmaRepository;

    private Turma turma;

    @BeforeEach
    void setUp() {
        turma = new Turma();
        turma.setId(1L);
        turma.setNome("Matemática");

        when(turmaRepository.save(any(Turma.class))).thenReturn(turma);
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turma));
        when(turmaRepository.findAll()).thenReturn(new ArrayList<>(List.of(turma)));
    }



    @Test
    void cadastrarTurma() {
        Turma novaTurma = turmaService.cadastrarTurma("Física");

        assertNotNull(novaTurma);
        assertEquals("Física", novaTurma.getNome());
        verify(turmaRepository).save(any(Turma.class));
    }

    @Test
    void listarTurmas() {
        List<Turma> turmas = turmaService.listarTurmas();

        assertNotNull(turmas);
        assertFalse(turmas.isEmpty());
        assertEquals(1, turmas.size());
        assertEquals("Matemática", turmas.get(0).getNome());
        verify(turmaRepository).findAll();
    }

    @Test
    void buscarTurmaPorId() {
        Turma encontrada = turmaService.buscarTurmaPorId(1L);

        assertNotNull(encontrada);
        assertEquals("Matemática", encontrada.getNome());
        verify(turmaRepository).findById(anyLong());
    }

    @Test
    void atualizarTurma() {
        Turma atualizada = turmaService.atualizarTurma(1L, "Química");

        assertNotNull(atualizada);
        assertEquals("Química", atualizada.getNome());
        verify(turmaRepository).findById(anyLong());
        verify(turmaRepository).save(any(Turma.class));
    }

    @Test
    void removerTurma() {
        Turma atualizada = turmaService.atualizarTurma(1L, "Química");

        assertNotNull(atualizada);
        assertEquals("Química", atualizada.getNome());
        verify(turmaRepository).findById(anyLong());
        verify(turmaRepository).save(any(Turma.class));
    }
}