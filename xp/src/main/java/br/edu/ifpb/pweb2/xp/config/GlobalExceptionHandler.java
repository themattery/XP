package br.edu.ifpb.pweb2.xp.config;

import br.edu.ifpb.pweb2.xp.exception.NotFoundException;
import br.edu.ifpb.pweb2.xp.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(NotFoundException ex, HttpServletRequest request) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 404);
        model.addObject("error", "Recurso não encontrado");
        model.addObject("message", ex.getMessage());
        model.addObject("path", request.getRequestURI());
        model.addObject("timestamp", LocalDateTime.now());
        return model;
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidation(ValidationException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        String referer = ex.getReferer() != null ? ex.getReferer() : "/corridas";
        return "redirect:" + referer;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 400);
        model.addObject("error", "Erro de validação");
        model.addObject("message", "Dados inválidos: " + errors);
        model.addObject("path", request.getRequestURI());
        model.addObject("timestamp", LocalDateTime.now());
        return model;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        String errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 400);
        model.addObject("error", "Erro de validação");
        model.addObject("message", "Dados inválidos: " + errors);
        model.addObject("path", request.getRequestURI());
        model.addObject("timestamp", LocalDateTime.now());
        return model;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        return "redirect:/corridas";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneric(Exception ex, HttpServletRequest request) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 500);
        model.addObject("error", "Erro interno do servidor");
        model.addObject("message", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        model.addObject("path", request.getRequestURI());
        model.addObject("timestamp", LocalDateTime.now());
        model.addObject("trace", ex.getStackTrace());
        
        System.err.println("ERRO INESPERADO: " + ex.getMessage());
        ex.printStackTrace();
        
        return model;
    }
}