package br.alura.forum.controller;

import br.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.alura.forum.controller.dto.TopicoDto;
import br.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.alura.forum.controller.form.TopicoForm;
import br.alura.forum.model.Topico;
import br.alura.forum.repository.CursoRepository;
import br.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeTopicos") //Avisa pro Spring guardar o retorno em Cash. O 'value' é o 'id' do Cache
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                                         page = 0, size = 10) Pageable paginacao){

        //Pageable: Interface do Spring que ajuda na paginacao e monta no tipo que
        //o repository.findAll aceita.
        //É possível enviar uma ordenacao. Esse params é possível graças ao decorator do arquivo Main


        if (nomeCurso == null){
            //Enviando paginacao ele retorna o tipo Page
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converter(topicos);
        }else{
            //O spring "cria sozinho" metodos se seguir a nomeclatura correta.
            Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
            return TopicoDto.converter(topicos);
        }


    }

    @PostMapping //O @Valid avisa ao Spring para fazer as validaçoes anotadas na classe TopicoForm
    @Transactional //Essa tag avisa que as alterações feitas na classe local devem ser feitas no banco de dados também
    @CacheEvict(value = "listaDeTopicos", allEntries = true) //Avisa que quando esse metodo for chamado deve apagar um cache especifico
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){
        //@RequestBody = Pega os dados do corpo e não da url
        //Após um post, no java deve-se retornar o link da url do novo item criado
        //Para o Spring, se usa o UriComponentsBuilder

        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        //Variavel acima: enviar a path do "www...topicos/{id}".
        //buildAndExpand: troca o valor do que esta entre chaves pelo valor colocado entre parenteses
        //.toUri converte toda a path e variaveis alocadas em uma Uri

        return ResponseEntity.created(uri).body(new TopicoDto(topico));
        //Este retorno é da URI com statuscode 200, retorna o Corpo com TopicoDTO
    }

    @GetMapping("/{id}") //@PathVariavle diz que a variavel ID nao vem em um "?" e sim em uma "/"
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()){
            return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional //Essa tag avisa que as alterações feitas na classe local devem ser feitas no banco de dados também
    @CacheEvict(value = "listaDeTopicos", allEntries = true) //Avisa que quando esse metodo for chamado deve apagar um cache especifico
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
        Optional<Topico> optional = topicoRepository.findById(id);
        if(optional.isPresent()){
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional //Essa tag avisa que as alterações feitas na classe local devem ser feitas no banco de dados também
    @CacheEvict(value = "listaDeTopicos", allEntries = true) //Avisa que quando esse metodo for chamado deve apagar um cache especifico
    public ResponseEntity remover(@PathVariable Long id){
        Optional<Topico> optional = topicoRepository.findById(id);
        if(optional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
