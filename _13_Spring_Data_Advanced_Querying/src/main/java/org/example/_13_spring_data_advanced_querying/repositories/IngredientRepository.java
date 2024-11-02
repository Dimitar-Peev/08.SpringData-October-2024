package org.example._13_spring_data_advanced_querying.repositories;

import org.example._13_spring_data_advanced_querying.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByNameStartingWith(String letters);

    List<Ingredient> findByNameInOrderByPriceAsc(List<String> names);

    @Transactional
    void deleteByName(String name);

    @Query("UPDATE Ingredient i SET i.price = i.price * 1.1")
    @Modifying
    @Transactional
    void updateIngredientsPriceBy10Percent();

    @Query("UPDATE Ingredient i SET i.price = i.price * 1.1 WHERE i.name IN :names")
    @Modifying
    @Transactional
    void updateIngredientsPricesForNames(List<String> names);
}
