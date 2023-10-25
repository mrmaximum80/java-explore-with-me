package ewm.main.category.service;

import ewm.main.category.dto.CategoryDto;
import ewm.main.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategory);

    void deleteCategory(Long catId);

    CategoryDto editCategory(CategoryDto category, Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
