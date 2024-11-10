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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class TurmaServiceTest {

    @Mock
    private TurmaRepository turmaRepository;

    @InjectMocks
    private TurmaService turmaService;

    private Turma turma;
    private Estudante estudante;

    @BeforeEach
    void setUp() {
        turma = new Turma();
        turma.setId(1L);
        turma.setNome("Matemática");

        estudante = new Estudante();
        estudante.setId(1L);
        estudante.setNome("João Silva");
        estudante.setMatricula("123456");
    }


    @Test
    void cadastrarTurma() {
        when(turmaRepository.save(any(Turma.class))).thenReturn(turma);

        Turma result = turmaService.cadastrarTurma("Física");

        assertNotNull(result);
        assertEquals("Física", result.getNome());
        verify(turmaRepository).save(any(Turma.class));
    }

    @Test
    void listarTurmas() {
        List<Turma> listaTurmas = new ArrayList<>();
        listaTurmas.add(turma);
        when(turmaRepository.findAll()).thenReturn(listaTurmas);

        List<Turma> result = turmaService.listarTurmas();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(turmaRepository).findAll();
    }

    @Test
    void buscarTurmaPorId() {
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turma));

        Turma result = turmaService.buscarTurmaPorId(1L);

        assertNotNull(result);
        assertEquals("Matemática", result.getNome());
        verify(turmaRepository).findById(anyLong());
    }

    @Test
    void atualizarTurma() {
        Turma turmaAtualizada = new Turma();
        turmaAtualizada.setId(1L);
        turmaAtualizada.setNome("Química");

        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turma));
        when(turmaRepository.save(any(Turma.class))).thenReturn(turmaAtualizada);

        Turma result = turmaService.atualizarTurma(1L, "Química");

        assertNotNull(result);
        assertEquals("Química", result.getNome());
        verify(turmaRepository).findById(anyLong());
        verify(turmaRepository).save(any(Turma.class));
    }

    @Test
    void removerTurma() {
        turmaService.removerTurma(1L);

        // Assert
        verify(turmaRepository).deleteById(anyLong());
    }

    @Test
    void adicionarEstudanteNaTurma() {
        estudante.setTurma(new ArrayList<>());

        Turma turmaMock = mock(Turma.class);
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turmaMock));

        Estudante result = turmaService.adicionarEstudanteNaTurma(1L, estudante);

        assertNotNull(result);
        assertTrue(result.getTurma().contains(turmaMock));
    }

    @Test
    void removerEstudanteDaTurma() {
        Turma turmaMock = mock(Turma.class);
        estudante.setTurma(new ArrayList<>(List.of(turmaMock)));

        Turma turmaToBeRemoved = mock(Turma.class);
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.of(turmaToBeRemoved));

        Estudante result = turmaService.removerEstudanteDaTurma(1L, estudante);

        assertNotNull(result);
        assertFalse(result.getTurma().contains(turmaToBeRemoved));
    }
}