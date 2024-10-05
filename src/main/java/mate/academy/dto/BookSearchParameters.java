package mate.academy.dto;

import mate.academy.validation.ValidNumericStringArray;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        @ValidNumericStringArray
        String[] prices
) {
}
