package edu.ifmg.produtos.dto;

import edu.ifmg.produtos.entities.Product;
import edu.ifmg.produtos.projections.ProductProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;
import java.util.Objects;

public class ProductListDTO extends RepresentationModel<ProductListDTO> {
    @Schema(description = "Database generated ID product")
    private long id;

    @Schema(description = "Product Name")
    @Size(min = 3, max = 255, message = "Deve ter entre 3 e 255 caracteres.")
    private String name;

    @Schema(description = "Product Price")
    @Positive(message = "Preço deve ter um valor positivo.")
    private double price;

    @Schema(description = "A image url represents this product.")
    private String imageUrl;


    public ProductListDTO() {
    }

    public ProductListDTO(String name, String description, double price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductListDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
    }

    public ProductListDTO(ProductProjection projection) {
        this.id = projection.getId();
        this.name = projection.getName();
        this.price = projection.getPrice();
        this.imageUrl = projection.getImageUrl();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductListDTO product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}