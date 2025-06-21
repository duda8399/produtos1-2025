package edu.ifmg.produtos.resources;

import edu.ifmg.produtos.dto.ProductDTO;
import edu.ifmg.produtos.dto.ProductListDTO;
import edu.ifmg.produtos.services.ProductService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/products")
@Tag(name = "Products", description = "API para gerenciamento de produtos")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping(produces = "application/json")
    @Operation(
            description = "Get all products",
            summary = "List all registered products",
            responses = {
                    @ApiResponse(description = "ok", responseCode = "200"),
            }
    )
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        Page<ProductDTO> products = productService.findAll(pageable);
        products.forEach(product -> this.addHateoasLinks(product));
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/paged", produces = "application/json")
    @Operation(
            description = "Get all products paged",
            summary = "List all registered products paged",
            responses = {
                    @ApiResponse(description = "ok", responseCode = "200"),
            }
    )
    public ResponseEntity<Page<ProductListDTO>> findAllPaged (
            Pageable pageable,
            @RequestParam(value = "categoryId", defaultValue = "0") String categoryId,
            @RequestParam(value="name", defaultValue = "") String name
    ) {
        Page<ProductListDTO> products = productService.findAllPaged(name, categoryId, pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO product = productService.findById(id);
        this.addHateoasLinks(product);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.insert(productDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        this.addHateoasLinks(product);

        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.update(id, productDTO);
        this.addHateoasLinks(product);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void addHateoasLinks(ProductDTO product) {
        product.add(linkTo(methodOn(ProductResource.class).findAll(Pageable.unpaged())).withRel("list"))
                .add(linkTo(methodOn(ProductResource.class).findById(product.getId())).withSelfRel())
                .add(linkTo(methodOn(ProductResource.class).insert(product)).withRel("insert"))
                .add(linkTo(methodOn(ProductResource.class).update(product.getId(), product)).withRel("update"))
                .add(linkTo(methodOn(ProductResource.class).delete(product.getId())).withRel("delete"));
    }
}