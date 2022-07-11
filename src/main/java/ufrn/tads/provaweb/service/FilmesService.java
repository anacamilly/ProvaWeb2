package ufrn.tads.provaweb.service;
import org.springframework.stereotype.Service;
import ufrn.tads.provaweb.models.Filmes;
import ufrn.tads.provaweb.repository.FilmesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilmesService {

    private final FilmesRepository repository;

    public FilmesService(FilmesRepository repository) {
        this.repository = repository;
    }

    public Filmes cadastrar(Filmes f){
        return repository.save(f);
    }

    public void deletarPorId(Integer id){
        repository.deleteById(id);
    }

    public Filmes editar(Filmes f){
        return repository.saveAndFlush(f);
    }

    public Filmes buscarPorId(Integer id){
        Optional<Filmes> FilmesOptional = repository.findById(id);
        return FilmesOptional.orElse(null);
    }

    public List<Filmes> findAll(){
        return repository.findAll();
    }

    public List <Filmes> Listall(){
        List<Filmes> lista_filmes = new ArrayList();
        List<Filmes> nova_lista_filmes = new ArrayList();

        lista_filmes = repository.findAll();
        var i = 0;

        for (i = 0; i <lista_filmes.size(); i++) {
            if( lista_filmes.get(i).getDeleted() == null){
                nova_lista_filmes.add( lista_filmes.get(i));
            }
        }

        return nova_lista_filmes;
    }
}