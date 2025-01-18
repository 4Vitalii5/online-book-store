package mate.academy.service.impl;

import static mate.academy.util.TestConstants.CATEGORY_NOT_FOUND_MESSAGE;
import static mate.academy.util.TestConstants.DUPLICATE_NAME_MESSAGE;
import static mate.academy.util.TestUtil.CATEGORY_DTO;
import static mate.academy.util.TestUtil.CATEGORY_PAGE;
import static mate.academy.util.TestUtil.CREATE_CATEGORY_REQUEST_DTO;
import static mate.academy.util.TestUtil.FIRST_CATEGORY;
import static mate.academy.util.TestUtil.PAGEABLE;
import static mate.academy.util.TestUtil.SECOND_CATEGORY;
import static mate.academy.util.TestUtil.UPDATED_CATEGORY_DTO;
import static mate.academy.util.TestUtil.UPDATE_CATEGORY_REQUEST_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.dto.category.CategoryDto;
import mate.academy.exception.DuplicateResourceException;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify creation of new category")
    void save_validCreateCategoryRequestDto_returnsCategoryDto() {
        // Given
        when(categoryMapper.toEntity(CREATE_CATEGORY_REQUEST_DTO)).thenReturn(FIRST_CATEGORY);
        when(categoryRepository.save(FIRST_CATEGORY)).thenReturn(FIRST_CATEGORY);
        when(categoryRepository.findByName(FIRST_CATEGORY.getName())).thenReturn(null);
        when(categoryMapper.toDto(FIRST_CATEGORY)).thenReturn(CATEGORY_DTO);

        // When
        CategoryDto savedCategoryDto = categoryService.save(CREATE_CATEGORY_REQUEST_DTO);

        // Then
        assertThat(savedCategoryDto).isEqualTo(CATEGORY_DTO);
        verify(categoryRepository, times(1))
                .findByName(FIRST_CATEGORY.getName());
        verify(categoryRepository, times(1)).save(FIRST_CATEGORY);
        verify(categoryMapper, times(1)).toDto(FIRST_CATEGORY);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify save() method when name already exists")
    void save_duplicateName_throwsDuplicateResourceException() {
        // Given
        String expected = String.format(DUPLICATE_NAME_MESSAGE, FIRST_CATEGORY.getName());
        when(categoryMapper.toEntity(CREATE_CATEGORY_REQUEST_DTO)).thenReturn(FIRST_CATEGORY);
        when(categoryRepository.findByName(FIRST_CATEGORY.getName())).thenReturn(FIRST_CATEGORY);

        // When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> categoryService.save(CREATE_CATEGORY_REQUEST_DTO));
        String actual = exception.getMessage();

        // Then
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1))
                .findByName(FIRST_CATEGORY.getName());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Find all categories")
    void findAll_validPageable_returnsAllCategories() {
        // Given
        when(categoryRepository.findAll(PAGEABLE)).thenReturn(CATEGORY_PAGE);
        when(categoryMapper.toDto(FIRST_CATEGORY)).thenReturn(CATEGORY_DTO);

        // When
        List<CategoryDto> categoryDtos = categoryService.findAll(PAGEABLE);

        // Then
        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos).containsExactly(CATEGORY_DTO);
        verify(categoryRepository, times(1)).findAll(PAGEABLE);
        verify(categoryMapper, times(1)).toDto(FIRST_CATEGORY);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Get category by valid id")
    void getById_validCategoryId_returnsCategoryDto() {
        // Given
        when(categoryRepository.findById(FIRST_CATEGORY.getId()))
                .thenReturn(Optional.of(FIRST_CATEGORY));
        when(categoryMapper.toDto(FIRST_CATEGORY)).thenReturn(CATEGORY_DTO);

        // When
        CategoryDto foundCategoryDto = categoryService.getById(FIRST_CATEGORY.getId());

        // Then
        assertThat(foundCategoryDto).isEqualTo(CATEGORY_DTO);
        verify(categoryRepository, times(1)).findById(FIRST_CATEGORY.getId());
        verify(categoryMapper, times(1)).toDto(FIRST_CATEGORY);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getById() method with invalid category id")
    void getById_invalidCategoryId_throwsEntityNotFoundException() {
        // Given
        String expected = String.format(CATEGORY_NOT_FOUND_MESSAGE, FIRST_CATEGORY.getId());
        when(categoryRepository.findById(FIRST_CATEGORY.getId())).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(FIRST_CATEGORY.getId()));
        String actual = exception.getMessage();

        // Then
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(FIRST_CATEGORY.getId());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Update category by valid id")
    void update_validCategoryIdAndCreateCategoryRequestDto_returnsUpdatedCategoryDto() {
        // Given
        when(categoryRepository.findById(FIRST_CATEGORY.getId()))
                .thenReturn(Optional.of(FIRST_CATEGORY));
        when(categoryRepository.findByName(UPDATE_CATEGORY_REQUEST_DTO.name()))
                .thenReturn(FIRST_CATEGORY);
        when(categoryRepository.save(FIRST_CATEGORY)).thenReturn(SECOND_CATEGORY);
        doNothing().when(categoryMapper)
                .updateCategoryFromDto(UPDATE_CATEGORY_REQUEST_DTO, FIRST_CATEGORY);
        when(categoryMapper.toDto(SECOND_CATEGORY)).thenReturn(UPDATED_CATEGORY_DTO);

        // When
        CategoryDto actual = categoryService.update(
                FIRST_CATEGORY.getId(), UPDATE_CATEGORY_REQUEST_DTO);

        // Then
        assertThat(actual).isEqualTo(UPDATED_CATEGORY_DTO);
        verify(categoryRepository, times(1)).findById(FIRST_CATEGORY.getId());
        verify(categoryRepository, times(2))
                .findByName(UPDATE_CATEGORY_REQUEST_DTO.name());
        verify(categoryRepository, times(1)).save(FIRST_CATEGORY);
        verify(categoryMapper, times(1)).toDto(SECOND_CATEGORY);
        verify(categoryMapper, times(1))
                .updateCategoryFromDto(UPDATE_CATEGORY_REQUEST_DTO, FIRST_CATEGORY);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify update() method with duplicated name")
    void update_duplicateName_throwsDuplicateResourceException() {
        // Given
        String expected = String.format(DUPLICATE_NAME_MESSAGE, UPDATE_CATEGORY_REQUEST_DTO.name());
        when(categoryRepository.findById(FIRST_CATEGORY.getId()))
                .thenReturn(Optional.of(FIRST_CATEGORY));
        when(categoryRepository.findByName(UPDATE_CATEGORY_REQUEST_DTO.name()))
                .thenReturn(SECOND_CATEGORY);

        // When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> categoryService.update(FIRST_CATEGORY.getId(), UPDATE_CATEGORY_REQUEST_DTO));
        String actual = exception.getMessage();

        // Then
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(FIRST_CATEGORY.getId());
        verify(categoryRepository, times(2))
                .findByName(UPDATE_CATEGORY_REQUEST_DTO.name());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Delete category by valid id")
    void deleteById_validCategoryId_deletesCategory() {
        // Given
        Long categoryId = FIRST_CATEGORY.getId();
        doNothing().when(categoryRepository).deleteById(categoryId);

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
