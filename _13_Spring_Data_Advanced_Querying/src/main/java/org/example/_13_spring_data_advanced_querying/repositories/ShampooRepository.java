package org.example._13_spring_data_advanced_querying.repositories;

import org.example._13_spring_data_advanced_querying.entities.Label;
import org.example._13_spring_data_advanced_querying.entities.Shampoo;
import org.example._13_spring_data_advanced_querying.entities.enums.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {

    List<Shampoo> findByBrand(String brand);

    List<Shampoo> findByBrandAndSize(String brand, Size size);

    List<Shampoo> findBySizeOrderByIdAsc(Size size);

    List<Shampoo> findBySizeOrderByIdDesc(Size size);

    List<Shampoo> findBySizeOrLabelId(Size size, long labelId);

    List<Shampoo> findBySizeOrLabelOrderByPriceAsc(Size size, Label label);

    List<Shampoo> findByLabelTitleContaining(String title);

    List<Shampoo> findByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    int countByPriceLessThan(BigDecimal price);

    Set<Shampoo> findByIngredientsNameIn(List<String> names);

    @Query("""
            SELECT s FROM Shampoo s
            JOIN s.ingredients i
            WHERE i.name IN :ingredients
            """)
    Set<Shampoo> findByIngredientsNameInQuery(@Param("ingredients") List<String> ingredientNames);

    @Query("SELECT s FROM Shampoo AS s WHERE SIZE(s.ingredients) < :count")
//  @Query(value = "SELECT * FROM shampoos s JOIN shampoos_ingredients...", nativeQuery = true)
    List<Shampoo> findByIngredientsCountLessThan(int count);

//----------------------------------------------------------------------------------------------------------------------
    @Query("""
            SELECT s.size, AVG(s.price)
                        FROM Shampoo AS s
                        GROUP BY s.size
            """)
    List<Object> findPriceBySize();

    @Query(value = "SELECT * FROM shampoos WHERE brand = :brand", nativeQuery = true)
    List<Shampoo> findByBrandNative(String brand);

}