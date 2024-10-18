package mate.academy.service;

import java.util.List;
import mate.academy.dto.category.CategoryDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CategoryDto requestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
