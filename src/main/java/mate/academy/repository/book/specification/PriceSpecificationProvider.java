package mate.academy.repository.book.specification;

import java.math.BigDecimal;
import mate.academy.model.Book;
import mate.academy.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final String PRICE_FIELD = "price";

    @Override
    public String getKey() {
        return PRICE_FIELD;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        BigDecimal minPrice = new BigDecimal(params[0]);
        BigDecimal maxPrice = new BigDecimal(params[1]);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(PRICE_FIELD), minPrice, maxPrice);
    }
}
