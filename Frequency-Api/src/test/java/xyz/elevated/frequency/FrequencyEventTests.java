package xyz.elevated.frequency;

import org.junit.Test;
import xyz.elevated.frequency.events.FrequencyAlertEvent;

public final class FrequencyEventTests {

    @Test(expected = NullPointerException.class)
    public void nullAlertTest() {
        final FrequencyAlertEvent event = new FrequencyAlertEvent(null, null, 0, 0);
    }
}
