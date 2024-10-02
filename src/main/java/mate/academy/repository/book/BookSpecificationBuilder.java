package mate.academy.repository.book;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.BookSearchParameters;
import mate.academy.model.Book;
import mate.academy.repository.SpecificationBuilder;
import mate.academy.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(searchParameters.titles()));
        }
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.prices() != null && searchParameters.prices().length == 2) {
            BigDecimal minPrice = searchParameters.prices()[0];
            BigDecimal maxPrice = searchParameters.prices()[1];
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("price"), minPrice, maxPrice));
        }
        return spec;
    }
}
