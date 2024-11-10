package com.example.demo.service;

import com.example.demo.database.entities.Estudante;
import com.example.demo.database.repositories.EstudanteRepository;
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
class EstudanteServiceTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @InjectMocks
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
    void cadastrarEstudante() {
        when(estudanteRepository.save(any(Estudante.class))).thenReturn(estudante);

        Estudante result = estudanteService.cadastrarEstudante("João Silva", "123456");

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
        assertEquals("123456", result.getMatricula());
        verify(estudanteRepository).save(any(Estudante.class));

    }

    @Test
    void listarEstudantes() {
        List<Estudante> listaEstudantes = new ArrayList<>();
        listaEstudantes.add(estudante);
        when(estudanteRepository.findAll()).thenReturn(listaEstudantes);

        List<Estudante> result = estudanteService.listarEstudantes();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(estudanteRepository).findAll();
    }

    @Test
    void buscarEstudantePorId() {
        when(estudanteRepository.findById(anyLong())).thenReturn(Optional.of(estudante));

        Estudante result = estudanteService.buscarEstudantePorId(1L);

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
        assertEquals("123456", result.getMatricula());
        verify(estudanteRepository).findById(anyLong());
    }

    @Test
    void atualizarEstudante() {
        Estudante estudanteAtualizado = new Estudante();
        estudanteAtualizado.setId(1L);
        estudanteAtualizado.setNome("Maria Santos");
        estudanteAtualizado.setMatricula("789012");

        when(estudanteRepository.findById(anyLong())).thenReturn(Optional.of(estudante));
        when(estudanteRepository.save(any(Estudante.class))).thenReturn(estudanteAtualizado);

        Estudante result = estudanteService.atualizarEstudante(1L, "Maria Santos", "789012");

        assertNotNull(result);
        assertEquals("Maria Santos", result.getNome());
        assertEquals("789012", result.getMatricula());
        verify(estudanteRepository).findById(anyLong());
        verify(estudanteRepository).save(any(Estudante.class));
    }

    @Test
    void removerEstudante() {
        estudanteService.removerEstudante(1L);

        verify(estudanteRepository).deleteById(anyLong());
    }
}