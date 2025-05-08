package edu.ifmg.produtos.services;

import edu.ifmg.produtos.dto.ProductDTO;
import edu.ifmg.produtos.entities.Product;
import edu.ifmg.produtos.repository.ProductRepository;
import edu.ifmg.produtos.services.exceptions.ResourceNotFound;
import edu.ifmg.produtos.utils.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        Product product = Factory.createProduct();
        page = new PageImpl<>(List.of(product,product));
    }

    @Test
    @DisplayName("Verificando se o objeto foi deletado no BD")
    void deleteShouldDoNothingWhenIdExists() {
        when(productRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(existingId);
        Assertions.assertDoesNotThrow(() -> productService.delete(existingId));
        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("Verificando se lança exceção se o objeto não existe no BD")
    void deleteShouldThrowExceptionWhenIdNonExists() {
        when(productRepository.existsById(nonExistingId)).thenReturn(false);
        //doNothing().when(productRepository).deleteById(nonExistingId);
        Assertions.assertThrows(ResourceNotFound.class, () -> productService.delete(nonExistingId));
        verify(productRepository, times(0)).deleteById(nonExistingId);
    }

    @Test
    @DisplayName("Verificando se o findAll retorna os dados paginados")
    void findAllShouldReturnOnePage() {

        when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Pageable pagina = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAll(pagina);
        Assertions.assertNotNull(result);
        verify(productRepository, times(1)).findAll(pagina);
    }
}
