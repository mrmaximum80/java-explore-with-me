package ewm.main.category.map;

import ewm.main.category.dto.CategoryDto;
import ewm.main.category.dto.NewCategoryDto;
import ewm.main.category.model.Category;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {

    public Category toCategory(NewCategoryDto categoryDto) {
        return new Category(categoryDto.getName());
    }

    public Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getName());
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
