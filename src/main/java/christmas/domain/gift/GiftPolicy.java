package christmas.domain.gift;

import christmas.domain.event.EventContext;

import java.util.List;

public interface GiftPolicy {
    List<Gift> apply(EventContext context);
}
