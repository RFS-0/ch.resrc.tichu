package ch.resrc.tichu.test.capabilities.habits.assertions;

import org.apache.commons.lang3.builder.*;

public class TestToStringStyle extends ToStringStyle {

    public TestToStringStyle() {
        super();
        this.setUseClassName(false);
        this.setUseIdentityHashCode(false);
        this.setUseFieldNames(false);
        this.setContentStart("|");
        this.setFieldSeparator("|");
        this.setContentEnd("|");
    }

}
