package ufrn.tads.provaweb.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.tads.provaweb.models.Filmes;
import ufrn.tads.provaweb.models.Usuarios;
import ufrn.tads.provaweb.service.FileStorageService;
import ufrn.tads.provaweb.service.FilmesService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@Controller
public class FilmesController {

    private final FilmesService filmesService;
    private final FileStorageService fileStorageService;

    public Usuarios usuarios;


    public FilmesController(FilmesService filmesService, FileStorageService fileStorageService){
        this.filmesService = filmesService;
        this.fileStorageService = fileStorageService;
    }


    @GetMapping(value = {"/", "/index"})
    public String home(Model model, HttpServletResponse response){
        List<Filmes> filmes = filmesService.Listall();
        model.addAttribute("filmes", filmes);

        Cookie cookie = new Cookie("carrinhoCompras","");
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        return "index";
    }

    @GetMapping(value = {"/cadastrar"})
    public String cadastrarFilmes(Model model){

        Filmes f = new Filmes();
        model.addAttribute("filmes", f);
        return "filmes/cadastrar";
    }

    @RequestMapping(value = "/pagAdmin", method = RequestMethod.GET)
    public String verFilmes(Model model){

        List<Filmes> filmes = filmesService.Listall();
        model.addAttribute("filmes", filmes);

        return "filmes/admin";
    }

    @RequestMapping(value = "/deletar/{id}", method = RequestMethod.GET)
    public String deletarFilmes(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Filmes filmes = filmesService.buscarPorId(id);
        filmes.setDeleted(new Date(System.currentTimeMillis()));
        filmesService.editar(filmes);

        redirectAttributes.addAttribute("msgDeletar", "Exclusão realizada com sucesso");

        return "redirect:/pagAdmin";
    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public String editarFilmes(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes){
        Filmes filmes = filmesService.buscarPorId(id);
        model.addAttribute("filmes", filmes);
        redirectAttributes.addAttribute("msgEditar", "Editado com sucesso");
        return "filmes/editar";
    }

    @PostMapping(value = "/salvar")
    public String salvarFilme(@ModelAttribute @Valid Filmes f, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (errors.hasErrors()) {
            System.out.println(errors.getAllErrors().stream().toArray());
            return "filmes/admin";
        } else {

			/*
			System.out.println(file.getOriginalFilename());
			System.out.println(file.getContentType());
			System.out.println(file.getSize());
			*/

            f.setImagemUri(file.getOriginalFilename());
            filmesService.editar(f);
            fileStorageService.save(file);

            redirectAttributes.addAttribute("msgEditar", "Salvo com sucesso");
            return "redirect:/pagAdmin";
        }
    }



}
