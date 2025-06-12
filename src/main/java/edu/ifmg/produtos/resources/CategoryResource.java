package edu.ifmg.produtos.resources;

import edu.ifmg.produtos.dto.CategoryDTO;
import edu.ifmg.produtos.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URI;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categorias", description = "Operações relacionadas a categorias de produtos")
public class CategoryResource {

    private final CategoryService categoryService;

    @Autowired
    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Listar categorias paginadas")
    public ResponseEntity<Page<CategoryDTO>> findAll(
            @Parameter(description = "Número da página") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Tamanho da página") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "Direção da ordenação") @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @Parameter(description = "Campo para ordenação") @RequestParam(value = "orderBy", defaultValue = "id") String orderBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<CategoryDTO> categories = categoryService.findAll(pageable);
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    public ResponseEntity<CategoryDTO> findById(
            @Parameter(description = "ID da categoria") @PathVariable Long id
    ) {
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO newCategory = categoryService.insert(categoryDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCategory.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newCategory);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok().body(updatedCategory);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
