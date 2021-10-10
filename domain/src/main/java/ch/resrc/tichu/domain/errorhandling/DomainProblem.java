package ch.resrc.tichu.domain.errorhandling;

import ch.resrc.tichu.capabilities.error_handling.*;

public enum DomainProblem implements Problem {

    INVALID_PROPERTY_MUTATION("${validationErrors}"),

    TIME_ENTRY_NOT_FOUND("Time entry with id=<${id}> does not exist in time bin <${timeBin}>"),

    DOUBLE_TIME_SPENDING_ON_SAME_BIN("Double time spending on time bin <${timeBin}>. Time entries overlap: ${timeEntry1} overlaps with ${timeEntry2}"),

    TIME_SPAN_TOO_LARGE("Time entry interval is unexpectedly larger than reporting period interval. ${timeEntry} > ${reportingPeriod}"),

    TIME_ENTRY_CROSSES_DAYS("Time entry must not cross day boundaries: <${timeInterval}>, timeBin=<${timeBinName}>, timeEntryId=<${timeEntryId}>"),

    DUPLICATE_TIME_ENTRY_ID("The time entry id must be globally unique. timeBinName=<${timeBinName}>, duplicate: <${ids}>."),

    DUPLICATE_TIME_BIN_NAME("The time bin name must be globally unique. Duplicate: <${name}>."),

    REPORTED_TIME_IS_NEGATIVE("A reported time cannot be negative, but was: <${totalTime}>"),

    TIME_FRAME_TOO_NARROW("Time frame is too narrow for the operation. timeFrame=<${timeFrame}>, mismatch=<${mismatch}>"),

    ILLEGAL_TIME_FRAME("Time frame must be aligned with day boundaries. timeFrame=<${timeFrame}>"),

    MANDATORY_VALUE_MISSING("${message}"),

    INVARIANT_VIOLATED("${message}");

    public static DomainProblem[] all() {
        return DomainProblem.values();
    }

    private final String details;

    DomainProblem(String details) {
        this.details = details;
    }

    @Override
    public String title() {
        return "Domain rule violated";
    }

    @Override
    public String detailsTemplate() {
        return details;
    }
}
