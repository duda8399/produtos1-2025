package edu.ifmg.produtos.services;

import edu.ifmg.produtos.dto.ProductDTO;
import edu.ifmg.produtos.dto.ProductListDTO;
import edu.ifmg.produtos.entities.Category;
import edu.ifmg.produtos.entities.Product;
import edu.ifmg.produtos.projections.ProductProjection;
import edu.ifmg.produtos.repositories.ProductRepository;
import edu.ifmg.produtos.services.exceptions.ResourceNotFound;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        return page.map(ProductDTO::new);
    }

    public Page<ProductListDTO> findAllPaged(String name, String categoryId, Pageable pageable) {
        List<Long> categoriesId = null;
        if(!categoryId.equals("0")){
            categoriesId = Arrays.stream(categoryId.split(","))
                    .map(id -> Long.parseLong(id))
                    .toList();
        }

        Page<ProductProjection> page = categoriesId != null
                ? productRepository.searchProductsWithCategories(categoriesId, name, pageable)
                : productRepository.searchProductsWithoutCategories(name, pageable);

        List<ProductListDTO> dtos = page
                .stream().map(p -> new ProductListDTO(p))
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> dbProduct = productRepository.findById(id);
        Product product = dbProduct.orElseThrow(() -> new ResourceNotFound("Product not found"));
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product();
        copyToEntity(product, productDTO);
        product = productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyToEntity(product, productDTO);
            product = productRepository.save(product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Product not found");
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFound("Product not found");
        }
    }

    private void copyToEntity(Product product, ProductDTO productDTO) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategories(productDTO.getCategories()
                .stream()
                .map(categoryDTO -> new Category(categoryDTO.getId(), categoryDTO.getName()))
                .collect(Collectors.toSet()));
    }
}