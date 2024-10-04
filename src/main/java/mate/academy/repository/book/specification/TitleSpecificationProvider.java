package mate.academy.repository.book.specification;

import java.util.Arrays;
import mate.academy.model.Book;
import mate.academy.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE_FIELD = "title";

    @Override
    public String getKey() {
        return TITLE_FIELD;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(TITLE_FIELD).in(Arrays.stream(params)
                .toArray());
    }
}
