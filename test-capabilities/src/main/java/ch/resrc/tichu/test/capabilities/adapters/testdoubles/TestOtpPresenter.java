package ch.resrc.tichu.test.capabilities.adapters.testdoubles;

import ch.resrc.tichu.capabilities.error_handling.*;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.documents.OtpDocument;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.outbound.OtpPresenter;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;

public class TestOtpPresenter extends TestErrorPresenter implements OtpPresenter {

    private Client client;
    private OtpDocument presented;
    private ValidationError validationError;

    @Override
    public void present(Client client, OtpDocument toBePresented) {
        this.client = client;
        this.presented = toBePresented;
    }

    @Override
    public void present(Client client, ValidationError validationError) {
        this.client = client;
        this.validationError = validationError;
    }

    public Client client() {
        return client;
    }

    public OtpDocument presented() {
        return presented;
    }

    public ValidationError validationError() {
        return validationError;
    }

    public Result<OtpDocument, ProblemDetected> presentedResult() {
        return presentedBusinessErrorResult().thenValueOf(() -> presented);
    }

    public static TestOtpPresenter spyingOn(OtpPresenter presenter) {

        return new TestOtpPresenter() {

            @Override
            public void present(Client client, OtpDocument toBePresented) {
                super.present(client, toBePresented);
                presenter.present(client, toBePresented);
            }

            @Override
            public void present(Client client, ValidationError validationError) {
                super.present(client, validationError);
                presenter.present(client, validationError);
            }

            @Override
            public void presentSystemFailure(Client client, RuntimeException failure) {
                super.presentSystemFailure(client, failure);
                presenter.presentSystemFailure(client, failure);
            }

            @Override
            public void presentBusinessError(Client client, BusinessError businessError) {
                super.presentBusinessError(client, businessError);
                presenter.presentBusinessError(client, businessError);
            }
        };
    }
}
