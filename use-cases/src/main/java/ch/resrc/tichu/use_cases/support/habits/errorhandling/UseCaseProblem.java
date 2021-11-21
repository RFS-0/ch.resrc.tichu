package ch.resrc.tichu.use_cases.support.habits.errorhandling;

import ch.resrc.tichu.capabilities.error_handling.Problem;

public enum UseCaseProblem implements Problem {

    USER_NOT_FOUND_OR_CREATED("User not found or created", "User with name <${name}> and email <${email}> could not be created."),

    INVALID_INPUT_DETECTED("Invalid input", "${validationMessage}"),
    ;

    private final String title;
    private final String detailsTemplate;

    UseCaseProblem(String title, String detailsTemplate) {
        this.title = title;
        this.detailsTemplate = detailsTemplate;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String detailsTemplate() {
        return detailsTemplate;
    }
}
