package mate.academy.dto;

import java.math.BigDecimal;

public record BookSearchParameters(String[] titles, String[] authors, BigDecimal[] prices) {
}
