package edu.ifmg.produtos.dto;

import edu.ifmg.produtos.entities.Category;

public class CategoryDTO {

    private Long id;
    private String name;

    public CategoryDTO() { }

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
