package edu.ifmg.produtos.utils;

import edu.ifmg.produtos.dto.ProductDTO;
import edu.ifmg.produtos.entities.Category;
import edu.ifmg.produtos.entities.Product;

public class Factory {

    public static Product createProduct(){
        Product p = new Product();
        p.setName("Produto Teste");
        p.setPrice(1000);
        p.setImageUrl("https://img.com/test.png");
        p.getCategories().add(new Category(60L, "CategoriaTeste"));
        return p;
    }

    public static ProductDTO createProductDTO(){
        Product p = createProduct();
        return new ProductDTO(p);
    }
}