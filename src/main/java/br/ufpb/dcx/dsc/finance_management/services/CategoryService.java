package br.ufpb.dcx.dsc.finance_management.services;

import br.ufpb.dcx.dsc.finance_management.DTOs.category.CategoryDTO;
import br.ufpb.dcx.dsc.finance_management.exceptions.UserNotFoundException;
import br.ufpb.dcx.dsc.finance_management.models.Category;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.repositories.CategoryRepository;
import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<User> userOptional = userRepository.findById(categoryDTO.getUserId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setUser(user);

            Category savedCategory = categoryRepository.save(category);
            return convertToDTO(savedCategory);
        } else {
            throw new UserNotFoundException("User with ID " + categoryDTO.getUserId() + " not found.");
        }
    }

    private CategoryDTO convertToDTO(Category category){
        return modelMapper.map(category, CategoryDTO.class);
    }

    private Category convertToEntity(CategoryDTO categoryDTO){
        return modelMapper.map(categoryDTO, Category.class);
    }

    public List<CategoryDTO> getCategories(Long userId) {
        List<Category> categories;
        if(userId == null){
            categories = categoryRepository.findAll();
        } else {
            categories = categoryRepository.findByUserId(userId);
        }
        return categories
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

 //   public List<CategoryDTO> getCategoriesByUserId(Long userId) {
   //     List<Category> categories = categoryRepository.findByUserId(userId);
     //   return categories.stream()
       //         .map(this::convertToDTO)
         //       .collect(Collectors.toList());
    //}

    public CategoryDTO getCategoryById(Long categoryId){
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            return convertToDTO(category);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(categoryDTO.getName());
            categoryRepository.save(category);
            return convertToDTO(category);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
