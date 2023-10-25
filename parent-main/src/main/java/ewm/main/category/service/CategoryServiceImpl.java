package ewm.main.category.service;

import ewm.main.category.dto.CategoryDto;
import ewm.main.category.dto.NewCategoryDto;
import ewm.main.category.map.CategoryMapper;
import ewm.main.category.model.Category;
import ewm.main.category.repository.CategoryRepository;
import ewm.main.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategory) {
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategory));
        log.info("New category created with id={}, name={}.", category.getId(), category.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        try {
            categoryRepository.deleteById(catId);
            log.info("Category with id={} deleted", catId);
        } catch (EmptyResultDataAccessException e) {
            log.info("Category with id={} was not found", catId);
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }

    @Override
    @Transactional
    public CategoryDto editCategory(CategoryDto categoryDto, Long catId) {
        if (categoryRepository.findById(catId).isEmpty()) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
        Category category = CategoryMapper.toCategory(categoryDto);
        category.setId(catId);
        categoryRepository.save(category);
        log.info("Category with id={} was edited. New category name is {}", category.getId(), category.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Long start = Long.valueOf(from);
        Long end = (long) (from + size);
        log.info("Getting categories...");
        return categoryRepository.findByIdBetween(start, end);
    }

    @Override
    @Transactional
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        log.info("Category with id={} was found. Category name is {}", category.getId(), category.getName());
        return CategoryMapper.toCategoryDto(category);
    }

}
