package ch.resrc.tichu.test.capabilities.adapters.testdoubles;

import ch.resrc.tichu.capabilities.events.*;
import ch.resrc.tichu.use_cases.support.outbound_ports.eventbus.*;
import org.apache.commons.lang3.builder.*;
import org.slf4j.*;

import java.util.*;

import static java.util.stream.Collectors.*;

public class FakeEventBus implements EventBus {

    private static final Logger LOG = LoggerFactory.getLogger(FakeEventBus.class);

    private List<Event> deliveryHistory = new ArrayList<>();


    @Override
    public void deliver(Event event) {

        deliveryHistory.add(event);
        LOG.debug("(_EVENT_) {}", event);
    }

    public List<Event> delivered() {

        return new ArrayList<>(deliveryHistory);
    }

    public <T extends Event> Optional<T> findFirstDelivered(Class<T> eventType) {

        return findAllDelivered(eventType).stream().findFirst();
    }

    public <T extends Event> List<T> findAllDelivered(Class<T> eventType) {

        return delivered().stream().filter(event -> event.is(eventType)).map(eventType::cast).collect(toList());
    }

    public <T extends Event> boolean hasDelivered(Class<T> eventType) {

        return !findAllDelivered(eventType).isEmpty();
    }

    public <T extends Event> boolean hasDeliveredOnce(Class<T> eventType) {

        return 1 == findAllDelivered(eventType).size();
    }

    public void forgetPreviousEvents() {
        deliveryHistory.clear();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("deliveryHistory", deliveryHistory)
                .toString();
    }
}
