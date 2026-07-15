package com.kevin.taskmanager.pokeapi;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PokeApiService {

    private final RestClient restClient  = RestClient.builder()
            .baseUrl("https://pokeapi.co/api/v2")
            .build();

    public ApiPokemon getPokemonByName(String name) {
        return restClient.get()
                .uri("/pokemon/{name}", name.toLowerCase())
                .retrieve()
                .body(ApiPokemon.class);
               
    }

}