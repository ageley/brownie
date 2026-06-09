package ai.gelej.brownie.ingredient;

import ai.gelej.brownie.ingredient.parsing.EnglishIngredientParser;
import ai.gelej.brownie.ingredient.parsing.RussianIngredientParser;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientConfirmationFormatterTest {

    private final IngredientConfirmationFormatter formatter = new IngredientConfirmationFormatter();

    @Test
    void format_englishFacts_rendersParseableMessageWithSparklesComment() {
        //given
        NutritionFacts facts = new NutritionFacts("Chocolate brownie",
                new BigDecimal("438"), new BigDecimal("19.0"), new BigDecimal("61"), new BigDecimal("5"));

        //when
        String message = formatter.format(facts, Language.ENGLISH);

        //then
        assertThat(message).isEqualTo("""
                Saved ✨
                Chocolate brownie
                Calories: 438 kcal
                Fat: 19 g
                Carbs: 61 g
                Protein: 5 g""");
    }

    @Test
    void format_englishFacts_roundTripsThroughParser() {
        //given
        NutritionFacts facts = new NutritionFacts("Chocolate brownie",
                new BigDecimal("438"), new BigDecimal("19"), new BigDecimal("61"), new BigDecimal("5"));

        //when
        String message = formatter.format(facts, Language.ENGLISH);

        //then
        assertThat(new EnglishIngredientParser().parse(message)).contains(facts);
    }

    @Test
    void format_russianFacts_roundTripsThroughParser() {
        //given
        NutritionFacts facts = new NutritionFacts("Шоколадный брауни",
                new BigDecimal("438"), new BigDecimal("19"), new BigDecimal("61"), new BigDecimal("5"));

        //when
        String message = formatter.format(facts, Language.RUSSIAN);

        //then
        assertThat(new RussianIngredientParser().parse(message)).contains(facts);
    }
}
