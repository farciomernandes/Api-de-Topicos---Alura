package br.alura.forum.repository;


import br.alura.forum.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    //Faça um find na entidade Topico, no relacionamento Curso, e dentro do curso pegue Nome
    Page<Topico> findByCurso_Nome(String nomeCurso, Pageable paginacao);

    //Criando a mesma query anterior sem seguir o padrao de nome mas usando JPQL
    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
    Page<Topico> carregarPorNomeDoCurso(String nomeCurso, Pageable paginacao);

}
