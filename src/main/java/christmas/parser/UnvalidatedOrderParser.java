package christmas.parser;

import christmas.domain.UnvalidatedOrder;
import christmas.exception.InvalidOrderException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnvalidatedOrderParser implements Parser<UnvalidatedOrder> {
    private static final String DELIMITER = ",";
    private static final Pattern pattern = Pattern.compile("(.*)-(\\d+)");

    @Override
    public UnvalidatedOrder parse(String input) {
        String strippedInput = input.strip();
        List<String> orderTokens = Arrays.asList(strippedInput.split(DELIMITER));

        return countOrder(orderTokens);
    }

    private UnvalidatedOrder countOrder(List<String> orderTokens) {
        UnvalidatedOrder unvalidatedOrder = new UnvalidatedOrder();

        for (String orderToken : orderTokens) {
            String strippedOrderToken = orderToken.strip();
            Matcher matcher = pattern.matcher(strippedOrderToken);
            if (!matcher.find()) {
                throw new InvalidOrderException();
            }
            putOrderWithValidation(unvalidatedOrder, matcher);
        }
        return unvalidatedOrder;
    }

    private void putOrderWithValidation(UnvalidatedOrder unvalidatedOrder, Matcher matcher) {
        try {
            String dishName = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            unvalidatedOrder.putOrder(dishName, quantity);
        } catch (NumberFormatException e) {
            throw new InvalidOrderException();
        }
    }
}
