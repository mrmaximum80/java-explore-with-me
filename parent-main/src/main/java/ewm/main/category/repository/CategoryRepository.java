package ewm.main.category.repository;

import ewm.main.category.dto.CategoryDto;
import ewm.main.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<CategoryDto> findByIdBetween(Long start, Long end);
}
