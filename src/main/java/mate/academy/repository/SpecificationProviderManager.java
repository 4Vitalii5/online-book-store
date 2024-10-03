package mate.academy.repository;

public interface SpecificationProviderManager<T> {
    <P> SpecificationProvider<T, P> getSpecificationProvider(String key);
}
