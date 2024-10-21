package br.ufpb.dcx.dsc.finance_management.service;

import br.ufpb.dcx.dsc.finance_management.DTOs.category.CategoryDTO;
import br.ufpb.dcx.dsc.finance_management.exceptions.UserNotFoundException;
import br.ufpb.dcx.dsc.finance_management.models.Category;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.repositories.CategoryRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
import br.ufpb.dcx.dsc.finance_management.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private User user;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Test User");

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setUser(user);

        categoryDTO = new CategoryDTO();
        categoryDTO.setUserId(1L);
        categoryDTO.setName("Test Category");
    }

    @Test
    void testCreateCategorySuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(modelMapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(categoryDTO);

        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);

        assertNotNull(createdCategory);
        assertEquals("Test Category", createdCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateCategoryUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                categoryService.createCategory(categoryDTO));

        assertEquals("User with ID 1 not found.", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testGetCategoryByIdSuccess() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelMapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(categoryDTO);

        CategoryDTO foundCategory = categoryService.getCategoryById(1L);

        assertNotNull(foundCategory);
        assertEquals("Test Category", foundCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                categoryService.getCategoryById(1L));

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void testDeleteCategorySuccess() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetCategoriesByUserId() {
        when(categoryRepository.findByUserId(1L)).thenReturn(List.of(category));
        when(modelMapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(categoryDTO);

        List<CategoryDTO> categories = categoryService.getCategories(1L);

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Test Category", categories.get(0).getName());
        verify(categoryRepository, times(1)).findByUserId(1L);
    }
}

