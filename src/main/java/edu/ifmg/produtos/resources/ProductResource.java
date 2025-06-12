package edu.ifmg.produtos.resources;

import edu.ifmg.produtos.dto.ProductDTO;
import edu.ifmg.produtos.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductResource {

    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Buscar todos os produtos",
            description = "Retorna uma lista paginada de todos os produtos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de produtos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            })
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        Page<ProductDTO> products = productService.findAll(pageable);
        products.forEach(product -> this.addHateoasLinks(product));
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Buscar produto por ID",
            description = "Retorna um produto com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO product = productService.findById(id);
        this.addHateoasLinks(product);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Adicionar um novo produto",
            description = "Cria um novo produto com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto criado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class)))
            })
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
