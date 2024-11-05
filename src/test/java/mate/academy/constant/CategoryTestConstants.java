package mate.academy.constant;

public class CategoryTestConstants {
    //Data for category creation
    public static final String CATEGORY_NAME = "Sci-Fi";
    public static final String CATEGORY_DESCRIPTION = "Books about science fiction";
    //Data for category update
    public static final String UPDATED_CATEGORY_NAME = "Updated Category";
    public static final String UPDATED_CATEGORY_DESCRIPTION = "Updated description";
    public static final Long CATEGORY_ID = 1L;
    public static final String CONTENT_TYPE_JSON = "application/json";
    //Sample category
    public static final Long SAMPLE_CATEGORY_ID = 1L;
    public static final String SAMPLE_CATEGORY_NAME = "Comedy";
    public static final String SAMPLE_CATEGORY_DESCRIPTION = "Books of comedy genre";
    //Exception messages
    public static final String DUPLICATE_NAME_MESSAGE = "Category with name: %s already exists";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "Can't find category by id: %d";
    public static final String INVALID_CATEGORY_NAME = "Darkness";
    //Data for second category
    public static final long SECOND_CATEGORY_ID = 3L;
    public static final String SECOND_CATEGORY_NAME = "Non-fiction";
    public static final String SECOND_CATEGORY_DESCRIPTION = "Documentary books";
    //Data for third category
    public static final long THIRD_CATEGORY_ID = 3L;
    public static final String THIRD_CATEGORY_NAME = "Horror";
    public static final String THIRD_CATEGORY_DESCRIPTION = "Horror books";

    private CategoryTestConstants() {
    }
}
