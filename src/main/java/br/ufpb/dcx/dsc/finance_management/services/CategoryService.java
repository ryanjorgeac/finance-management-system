package br.ufpb.dcx.dsc.finance_management.services;

import br.ufpb.dcx.dsc.finance_management.DTOs.CategoryDTO;
import br.ufpb.dcx.dsc.finance_management.models.Category;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.repositories.CategoryRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<User> userOptional = userRepository.findById(categoryDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setUser(user);

            Category savedCategory = categoryRepository.save(category);
            return new CategoryDTO(savedCategory.getId(), savedCategory.getName(), user.getId());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<CategoryDTO> getCategoriesByUserId(Long userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);
        return categories.stream()
                .map(cat -> new CategoryDTO(cat.getId(), cat.getName(), cat.getUser().getId()))
                .collect(Collectors.toList());
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(categoryDTO.getName());
            categoryRepository.save(category);
            return new CategoryDTO(category.getId(), category.getName(), category.getUser().getId());
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
