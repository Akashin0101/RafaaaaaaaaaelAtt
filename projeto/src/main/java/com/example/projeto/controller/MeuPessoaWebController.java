package com.example.projeto.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projeto.model.Pessoa;
import com.example.projeto.service.PessoaService;

import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/meu-crud")
public class MeuPessoaWebController {

    private final PessoaService pessoaService;

    public MeuPessoaWebController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping("/listar")
    public String listarPessoas(Model model) {
        model.addAttribute("lista", pessoaService.listarPessoas());
        return "meu-crud/lista"; 
    }

    @GetMapping("/cadastrar")
    public String exibirFormCadastro(Model model) {
        model.addAttribute("pessoa", new Pessoa());
        return "meu-crud/form";
    }

    @PostMapping("/cadastrar")
    public String cadastrarPessoa(
            @Valid @ModelAttribute("pessoa") Pessoa pessoa,
            BindingResult result,
            RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "meu-crud/form"; 
        }
        pessoaService.salvarPessoa(pessoa);
        ra.addFlashAttribute("success", "Pessoa cadastrada com sucesso!");
        return "redirect:/meu-crud/listar";
    }

    @GetMapping("/{id}")
    public String detalhesPessoa(@PathVariable Long id, Model model) {
        Pessoa p = pessoaService.buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Pessoa não encontrada, id: " + id
            ));
        model.addAttribute("pessoa", p);
        return "meu-crud/detalhe";
    }

    @PostMapping("/{id}/excluir")
    public String excluirPessoa(@PathVariable Long id, RedirectAttributes ra) {
        pessoaService.deletarPessoa(id);
        ra.addFlashAttribute("success", "Pessoa excluída com sucesso!");
        return "redirect:/meu-crud/listar";                
    }
}