package ufrn.tads.provaweb;

import antlr.BaseAST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ufrn.tads.provaweb.models.Filmes;
import ufrn.tads.provaweb.models.Usuarios;
import ufrn.tads.provaweb.repository.FilmesRepository;
import ufrn.tads.provaweb.repository.UsuariosRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class ProvawebApplication implements WebMvcConfigurer {

	@Autowired
	private FilmesRepository filmeRepository;
	@Autowired
	private UsuariosRepository repository;


	public static void main(String[] args) {
		SpringApplication.run(ProvawebApplication.class, args);
	}

	@PostConstruct
	public void initFilmes() {

		List<Filmes> filmes = Stream.of(
				new Filmes(1, "Star Wars", "George Lucas", "25/05/1977", "50", "starwars.png", null, "STAR WARS é uma franquia do tipo space opera estadunidense criada pelo cineasta George Lucas, que conta com uma série de nove filmes de fantasia científica e dois spin-offs.", 5 ),
				new Filmes(2, "Perdido em Marte", "Ridley Scott", "01/10/2015", "50", "perdidoemmarte.jpg", null, "O astronauta Mark Watney é enviado a uma missão para Marte, mas após uma severa tempestade, ele é dado como morto, abandonado pelos colegas e acorda sozinho no planeta inóspito.", 5 )
		).collect(Collectors.toList());

		filmeRepository.saveAll(filmes);

		List<Usuarios> usuarios = Stream.of(
				new Usuarios(1L, "admin", encoder().encode("admin"), false, false, false, true, true),
				new Usuarios(2L, "ana", encoder().encode("ana"), false, false, false, true, false),
				new Usuarios(3L, "user2", encoder().encode("user2"), false, false, false, true, false)

		).collect(Collectors.toList());

		repository.saveAll(usuarios);

	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Register resource handler for images
		registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
				//.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
		/*
		registry.addResourceHandler("/images/**").addResourceLocations("/images/")
		.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());*/
	}

	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
