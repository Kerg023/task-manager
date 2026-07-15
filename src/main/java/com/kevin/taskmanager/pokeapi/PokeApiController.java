package com.kevin.taskmanager.pokeapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PokeApiController {
    private final PokeApiService pokeApiService;

    public PokeApiController(PokeApiService pokeApiService) {
        this.pokeApiService = pokeApiService;
    }

    @GetMapping("/pokedex")
    public String home() {
        return "pokedex-home";
    }

    @GetMapping("/pokedex/buscar")
    public String buscar(@RequestParam String nombre) {     
        return "redirect:/pokedex?nombre=" + nombre;
    }

    @GetMapping("/pokedex/{name}")
    public String getPokemon(@PathVariable String name, Model model) {
        try{
            model.addAttribute("pokemon", pokeApiService.getPokemonByName(name));
        } catch (Exception e) {
            model.addAttribute("error", "No se encontro un pokemon conel nombre: " + name);
        }
        return "pokedex-result";
    }

}
