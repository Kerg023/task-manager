package com.kevin.taskmanager.pokeapi;
import java.util.List;

public class ApiPokemon {

    private String name;
    private int height;
    private int weight;
    private List<Ability> abilities;


    public ApiPokemon(String name, int height, int weight, List<Ability> abilities) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getHeight() {
        return height;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public int getWeight() {
        return weight;
    }


    public void setWeight(int weight) {
        this.weight = weight;
    }


    public List<Ability> getAbilities() {
        return abilities;
    }


    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public static class Ability {

        private AbilityInfo ability;

        public AbilityInfo getAbility() {
            return ability;
        }

        public void setAbility(AbilityInfo ability) {
            this.ability = ability;
        }
    }

   
    public static class AbilityInfo {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    

    
    









    

}
