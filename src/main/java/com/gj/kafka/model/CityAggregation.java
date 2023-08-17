package com.gj.kafka.model;

public class CityAggregation {

    Long population;


    public CityAggregation(Long population) {
        this.population = population;
    }

    public CityAggregation(Integer population) {
        this.population = new Long(population);
    }
    public CityAggregation() {
        this.population = 0L;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "CityAggregation{" +
                "population=" + population +
                '}';
    }
}
