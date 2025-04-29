package edu.ifmg.produtos.resources;

import edu.ifmg.produtos.dto.CategoryDTO;
import edu.ifmg.produtos.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Inserir nova categoria")
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {
        dto = categoryService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria existente")
    public ResponseEntity<CategoryDTO> update(
            @Parameter(description = "ID da categoria") @PathVariable Long id,
            @RequestBody CategoryDTO dto
    ) {
        dto = categoryService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma categoria")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da categoria") @PathVariable Long id
    ) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
