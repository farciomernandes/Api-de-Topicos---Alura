package br.alura.forum.repository;

import br.alura.forum.model.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

@SpringBootTest
public class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {

        String nomeCurso= "HTML 5";
        Curso curso= cursoRepository.findByNome(nomeCurso);
        AssertionErrors.assertEquals("Nome do curso veio certo!!!!", curso.getNome(), nomeCurso);
    }

    @Test
    public void naoDeveriaCarregarUmCursoComNomeNaoCadastrado() {

        String nomeCurso= "CURSO INVALIDO";
        Curso curso = cursoRepository.findByNome(nomeCurso);
        AssertionErrors.assertNull("Nome do curso invalido nao encontrado!", curso);
    }

}