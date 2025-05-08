package edu.ifmg.produtos.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.Objects;
import java.util.HashSet;
import io.swagger.v3.oas.annotations.media.Schema;

import edu.ifmg.produtos.entities.Product;
import edu.ifmg.produtos.entities.Category;
import org.springframework.hateoas.RepresentationModel;

public class ProductDTO extends RepresentationModel<ProductDTO> {
    @Schema(description = "Database generated ID product")
    private Long id;
    @Schema(description = "Product name")
    private String name;
    @Schema(description = "A detail description of the product")
    private String description;
    @Schema(description = "Product price")
    private Double price;
    @Schema(description = "Product image URL")
    private String imageUrl;
    @Schema(description = "Product categories (one or more)")
    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO() {

    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
        entity.getCategories().forEach(c -> categories.add(new CategoryDTO(c)));
    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(c -> this.categories.add(new CategoryDTO(c)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductDTO product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories=" + categories +
                '}';
    }
}
