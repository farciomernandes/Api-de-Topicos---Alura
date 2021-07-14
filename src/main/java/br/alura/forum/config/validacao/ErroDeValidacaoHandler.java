package br.alura.forum.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice //O mesmo que um middleware
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;
    //MessageSource: recurso do Spring que ajuda a pegar mensagem de erros e formatar em varios idiomas

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class) //Quando esse acontecer em qualquer lugar, essa funcao vai ser chamada
    public List<ErroDeFormulatioDto> handle(MethodArgumentNotValidException exception){
        List<ErroDeFormulatioDto> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e->{
            //messageSource.getMessage: Recebe o erro e a função que descobre o idioma do local onde o erro foi acionado
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormulatioDto erro = new ErroDeFormulatioDto(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }
}
