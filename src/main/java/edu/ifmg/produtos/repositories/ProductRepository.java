package edu.ifmg.produtos.repositories;

import edu.ifmg.produtos.entities.Product;
import edu.ifmg.produtos.projections.ProductProjection;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true,
            value = """
            select * from (
                select distinct p.id , p.name , p.imageurl , p.price
                from products p
                inner Join categories_to_products cp on cp.product_id = p.id
                where (cp.category_id  in :categoriesID) and lower(p.name) like lower(CONCAT ('%',:name,'%'))
            ) as result
        """,
            countQuery = """
            select count(*) from (
                select distinct p.id , p.name , p.imageurl , p.price
                from products p
                inner Join categories_to_products cp on cp.product_id = p.id
                where (cp.category_id  in :categoriesID) and lower(p.name) like lower (CONCAT ('%',:name,'%'))
            ) as result
        """
    )
    public Page<ProductProjection> searchProductsWithCategories(List<Long> categoriesID, String name, Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                select * from (
                    select distinct p.id , p.name , p.imageurl , p.price
                    from products p
                    inner join categories_to_products cp on cp.product_id = p.id
                    where lower(p.name) like lower(CONCAT ('%',:name,'%'))
                ) as result
            """,
            countQuery = """
                select count(*) from (
                    select distinct p.id , p.name , p.imageurl , p.price
                    from products p
                    inner join categories_to_products cp on cp.product_id = p.id
                    where lower(p.name) like lower(CONCAT ('%',:name,'%'))
                ) as result
            """
    )
    Page<ProductProjection> searchProductsWithoutCategories(String name, Pageable pageable);
}