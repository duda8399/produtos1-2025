package edu.ifmg.produtos.repositories;

import edu.ifmg.produtos.entities.Product;
import edu.ifmg.produtos.utils.Factory;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Verificando se o objeto não existe no BD depois de deletado.")
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(1L);
        Optional<Product> obj = productRepository.findById(1L);
        Assertions.assertFalse(obj.isPresent());
    }

    @Test
    @DisplayName("Verificando o autoincremento da chave primária.")
    public void insertShouldPersistWithAutoincrementIdZero() {
        Product product = Factory.createProduct();
        product.setId(0);

        Product p = productRepository.save(product);
        Optional<Product> obj = productRepository.findById(p.getId());
        Assertions.assertTrue(obj.isPresent());
        Assertions.assertNotEquals(0, obj.get().getId());
        Assertions.assertEquals(26, obj.get().getId());
    }
}