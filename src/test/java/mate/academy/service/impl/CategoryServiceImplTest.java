package mate.academy.service.impl;

import static mate.academy.constant.BookTestConstants.SEARCH_PAGE_NUMBER;
import static mate.academy.constant.BookTestConstants.SEARCH_PAGE_SIZE;
import static mate.academy.constant.CategoryTestConstants.DUPLICATE_NAME_MESSAGE;
import static mate.academy.constant.CategoryTestConstants.ENTITY_NOT_FOUND_MESSAGE;
import static mate.academy.constant.CategoryTestConstants.SAMPLE_CATEGORY_DESCRIPTION;
import static mate.academy.constant.CategoryTestConstants.SAMPLE_CATEGORY_ID;
import static mate.academy.constant.CategoryTestConstants.SAMPLE_CATEGORY_NAME;
import static mate.academy.constant.CategoryTestConstants.THIRD_CATEGORY_DESCRIPTION;
import static mate.academy.constant.CategoryTestConstants.THIRD_CATEGORY_ID;
import static mate.academy.constant.CategoryTestConstants.THIRD_CATEGORY_NAME;
import static mate.academy.constant.CategoryTestConstants.UPDATED_CATEGORY_DESCRIPTION;
import static mate.academy.constant.CategoryTestConstants.UPDATED_CATEGORY_NAME;
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
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.exception.DuplicateResourceException;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category sampleCategory;
    private CategoryDto sampleCategoryDto;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category(
                SAMPLE_CATEGORY_ID, SAMPLE_CATEGORY_NAME, SAMPLE_CATEGORY_DESCRIPTION
        );
        sampleCategoryDto = new CategoryDto(
                sampleCategory.getId(), sampleCategory.getName(),
                sampleCategory.getDescription()
        );
    }

    @Test
    @DisplayName("Verify creation of new category")
    void save_validCreateCategoryRequestDto_returnsCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                sampleCategory.getName(), sampleCategory.getDescription()
        );

        when(categoryMapper.toEntity(requestDto)).thenReturn(sampleCategory);
        when(categoryRepository.save(sampleCategory)).thenReturn(sampleCategory);
        when(categoryRepository.findByName(sampleCategory.getName())).thenReturn(null);
        when(categoryMapper.toDto(sampleCategory)).thenReturn(sampleCategoryDto);

        // When
        CategoryDto savedCategoryDto = categoryService.save(requestDto);

        // Then
        assertThat(savedCategoryDto).isEqualTo(sampleCategoryDto);
        verify(categoryRepository, times(1))
                .findByName(sampleCategory.getName());
        verify(categoryRepository, times(1)).save(sampleCategory);
        verify(categoryMapper, times(1)).toDto(sampleCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify save() method when name already exists")
    void save_duplicateName_throwsDuplicateResourceException() {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                sampleCategory.getName(), sampleCategory.getDescription()
        );
        String expected = String.format(DUPLICATE_NAME_MESSAGE, sampleCategory.getName());

        when(categoryMapper.toEntity(requestDto)).thenReturn(sampleCategory);
        when(categoryRepository.findByName(sampleCategory.getName())).thenReturn(sampleCategory);

        // When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> categoryService.save(requestDto));
        String actual = exception.getMessage();

        // Then
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1))
                .findByName(sampleCategory.getName());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Find all categories")
    void findAll_validPageable_returnsAllCategories() {
        // Given
        Pageable pageable = PageRequest.of(SEARCH_PAGE_NUMBER, SEARCH_PAGE_SIZE);
        Page<Category> categoryPage = new PageImpl<>(List.of(sampleCategory), pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(sampleCategory)).thenReturn(sampleCategoryDto);

        // When
        List<CategoryDto> categoryDtos = categoryService.findAll(pageable);

        // Then
        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos).containsExactly(sampleCategoryDto);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(sampleCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Get category by valid id")
    void getById_validCategoryId_returnsCategoryDto() {
        // Given
        when(categoryRepository.findById(sampleCategory.getId()))
                .thenReturn(Optional.of(sampleCategory));
        when(categoryMapper.toDto(sampleCategory)).thenReturn(sampleCategoryDto);

        // When
        CategoryDto foundCategoryDto = categoryService.getById(sampleCategory.getId());

        // Then
        assertThat(foundCategoryDto).isEqualTo(sampleCategoryDto);
        verify(categoryRepository, times(1)).findById(sampleCategory.getId());
        verify(categoryMapper, times(1)).toDto(sampleCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getById() method with invalid category id")
    void getById_invalidCategoryId_throwsEntityNotFoundException() {
        // Given
        Long categoryId = sampleCategory.getId();
        String expected = String.format(ENTITY_NOT_FOUND_MESSAGE, categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));
        String actual = exception.getMessage();

        // Then
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Update category by valid id")
    void update_validCategoryIdAndCreateCategoryRequestDto_returnsUpdatedCategoryDto() {
        // Given
        Long categoryId = sampleCategory.getId();
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                UPDATED_CATEGORY_NAME, UPDATED_CATEGORY_DESCRIPTION
        );
        Category updatedCategory = new Category(
                categoryId, requestDto.name(), requestDto.description()
        );
        CategoryDto expectedCategoryDto = new CategoryDto(
                updatedCategory.getId(), updatedCategory.getName(), updatedCategory.getDescription()
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(sampleCategory));
        when(categoryRepository.findByName(requestDto.name())).thenReturn(sampleCategory);
        when(categoryRepository.save(sampleCategory)).thenReturn(updatedCategory);
        doNothing().when(categoryMapper).updateCategoryFromDto(requestDto, sampleCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedCategoryDto);

        // When
        CategoryDto actual = categoryService.update(categoryId, requestDto);

        // Then
        assertThat(actual).isEqualTo(expectedCategoryDto);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(2))
                .findByName(requestDto.name());
        verify(categoryRepository, times(1)).save(sampleCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
        verify(categoryMapper, times(1))
                .updateCategoryFromDto(requestDto, sampleCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify update() method with duplicated name")
    void update_duplicateName_throwsDuplicateResourceException() {
        // Given
        Long categoryId = sampleCategory.getId();
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                THIRD_CATEGORY_NAME, THIRD_CATEGORY_DESCRIPTION
        );
        Category anotherCategory = new Category(
                THIRD_CATEGORY_ID, requestDto.name(), requestDto.description()
        );
        String expected = String.format(DUPLICATE_NAME_MESSAGE, requestDto.name());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(sampleCategory));
        when(categoryRepository.findByName(requestDto.name()))
                .thenReturn(anotherCategory);

        // When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> categoryService.update(categoryId, requestDto));
        String actual = exception.getMessage();

        // Then
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(2)).findByName(requestDto.name());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Delete category by valid id")
    void deleteById_validCategoryId_deletesCategory() {
        // Given
        Long categoryId = sampleCategory.getId();

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
