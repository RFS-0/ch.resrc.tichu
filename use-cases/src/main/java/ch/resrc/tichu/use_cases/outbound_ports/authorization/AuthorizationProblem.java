package ch.resrc.tichu.use_cases.outbound_ports.authorization;

import ch.resrc.tichu.capabilities.error_handling.*;

public enum AuthorizationProblem implements Problem {

    ACCESS_DENIED("Access denied",
                  "You don't have sufficient privileges to access the requested data.");

    private final String title;
    private final String detailsTemplate;


    AuthorizationProblem(String title, String detailsTemplate) {

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
