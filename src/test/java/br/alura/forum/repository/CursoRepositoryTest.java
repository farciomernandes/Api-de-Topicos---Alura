package br.alura.forum.repository;

import br.alura.forum.model.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") //spring, nos testes, força a classe a usar esse profile
@DataJpaTest
public class CursoRepositoryTest {
//AutoConfigure: Spring, nao tire o banco de dados atual para adicionar o h2
//Se tivesse sem esse decorator, ele iria substituir por um banco em memoria
    @Autowired
    private CursoRepository cursoRepository;

    //Inicia o banco de dados com os valores configurados para
    //iniciar o cenário de testes
    @Autowired
    private TestEntityManager em;

    @Test
    public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
        String nomeCurso= "HTML 5";
        Curso html5 = new Curso();
        html5.setNome("HTML 5");
        System.out.println("SACA SO:    " + html5.getNome());
        html5.setCategoria("Programacao");
        em.persist(html5);
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