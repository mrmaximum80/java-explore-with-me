package ewm.main.category.controller;

import ewm.main.category.dto.CategoryDto;
import ewm.main.category.dto.NewCategoryDto;
import ewm.main.category.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategory) {
        log.info("Creating new category - {}...", newCategory.getName());
        return categoryService.createCategory(newCategory);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        log.info("Category with id={} deleted", catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto editCategory(@RequestBody @Valid CategoryDto category,
                                    @PathVariable Long catId) {
        log.info("Editing category - {}...", category.getName());
        return categoryService.editCategory(category, catId);
    }

}
