package mate.academy.repository.book;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.model.Book;
import mate.academy.repository.SpecificationProvider;
import mate.academy.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book, ?>> bookSpecificationProviders;

    @Override
    public <P> SpecificationProvider<Book, P> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(k -> k.getKey().equals(key))
                .findFirst()
                .map(provider -> (SpecificationProvider<Book, P>) provider)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Can't find correct specification provider for key: " + key)
                );
    }
}
