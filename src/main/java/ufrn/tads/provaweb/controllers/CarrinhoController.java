package ufrn.tads.provaweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ufrn.tads.provaweb.models.Filmes;
import ufrn.tads.provaweb.service.FilmesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static org.springframework.web.util.WebUtils.getCookie;

@Controller
public class CarrinhoController {

    private final FilmesService filmesService;

    public CarrinhoController(FilmesService filmesService) {
        this.filmesService = filmesService;
    }

    @GetMapping("/addItemCarrinho")
    public void doAdicionarItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var id = 1;
        var filme = filmesService.buscarPorId(id);
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        carrinhoCompras.setMaxAge(60 * 60 * 24);
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;
        if (requestCookies != null) {
            for (var c : requestCookies) {
                achouCarrinho = true;
                carrinhoCompras = c;
                break;
            }
        }
        Filmes filmes = null;
        if (filme != null){
            filmes = filme;
            if (achouCarrinho == true){
                String value = carrinhoCompras.getValue();
                carrinhoCompras.setValue(value + filmes.getId() + "|");
            }else{
                carrinhoCompras.setValue(filmes.getId() + "|");
            }
        }else {
            response.addCookie(carrinhoCompras);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index");
            dispatcher.forward(request, response);
        }
        response.addCookie(carrinhoCompras);

    }

    @GetMapping("/visualizarCarrinho")
    public String visualizarCarrinho(HttpServletRequest request, Model model) throws ServletException, IOException {
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;
        if (requestCookies != null) {
            for (var c : requestCookies) {
                achouCarrinho = true;
                carrinhoCompras = c;
                break;
            }
        }
        Filmes filme = null;
        var i = 0;
        ArrayList<Filmes> lista_filme = new ArrayList();
        if(achouCarrinho == true) {
            StringTokenizer tokenizer = new StringTokenizer(carrinhoCompras.getValue(), "|");
            while (tokenizer.hasMoreTokens()) {
                filme = filmesService.buscarPorId(Integer.parseInt(tokenizer.nextToken()));
                lista_filme.add(filme);
            }
            model.addAttribute("carrinho", lista_filme);
            return "carrinho";

        } else {
            return "index";
        }
    }

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpServletRequest request, HttpServletResponse response){
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        response.addCookie(carrinhoCompras);
        return "redirect:/index";
    }
}