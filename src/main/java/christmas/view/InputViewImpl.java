package christmas.view;

import camp.nextstep.edu.missionutils.Console;
import christmas.domain.order.UnvalidatedOrder;
import christmas.domain.vo.VisitDate;
import christmas.exception.DiscountException;
import christmas.parser.UnvalidatedOrderParser;
import christmas.parser.Parser;
import christmas.parser.VisitDateParser;

public class InputViewImpl implements InputView {
    @Override
    public VisitDate readDate() {
        String visitDatePrompt = "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.\n" +
                "12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)";
        return readInput(visitDatePrompt, new VisitDateParser());
    }

    @Override
    public UnvalidatedOrder readUnvalidatedOrder() {
        String orderPrompt = "주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)";
        return readInput(orderPrompt, new UnvalidatedOrderParser());
    }

    private <T> T readInput(String prompt, Parser<T> parser) {
        System.out.println(prompt);

        while (true) {
            try {
                return parser.parse(Console.readLine());
            } catch (DiscountException de) {
                System.out.println(de.getMessage());
            }
        }
    }
}
