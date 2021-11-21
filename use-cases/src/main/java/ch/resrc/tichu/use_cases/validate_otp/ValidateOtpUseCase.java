package ch.resrc.tichu.use_cases.validate_otp;

import ch.resrc.tichu.capabilities.error_handling.ProblemDiagnosis;
import ch.resrc.tichu.capabilities.error_handling.faults.OurFault;
import ch.resrc.tichu.capabilities.events.Notifiable;
import ch.resrc.tichu.capabilities.functional.VoidMatch;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.domain.entities.UserId;
import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.use_cases.support.habits.errorhandling.UseCaseProblem;
import ch.resrc.tichu.use_cases.support.habits.events.Forum;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;
import ch.resrc.tichu.use_cases.support.outbound_ports.persistence.OtpRepository;
import ch.resrc.tichu.use_cases.validate_otp.ports.events.OtpValidated;
import ch.resrc.tichu.use_cases.validate_otp.ports.inbound.ValidateOtp;
import ch.resrc.tichu.use_cases.validate_otp.ports.outbound.OtpValidationPresenter;

import static ch.resrc.tichu.capabilities.functional.ForEach.forEach;
import static ch.resrc.tichu.capabilities.functional.VoidMatch.DefaultIgnore;
import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public class ValidateOtpUseCase implements ValidateOtp {

    private final OutboundPorts ports;

    private ValidateOtpUseCase(OutboundPorts ports) {
        this.ports = ports;
    }

    public static ValidateOtp create(OutboundPorts ports) {
        return new ValidateOtpUseCase(ports);
    }

    @Override
    public void invoke(Input input, OtpValidationPresenter presenter) {
        OtpSource otpSource = new OtpSource(ports.otpRepository);

        Forum forum = new Forum();

        UserInterface ui = new UserInterface(input.client(), presenter);
        forum.events().subscribe(ui);

        Workflow workflow = new Workflow((otpSource), forum);

        workflow.validateOtp(input.userId(), input.otp());
    }

    static class Workflow {

        final Forum forum;
        final OtpSource otpSource;

        void validateOtp(UserId userId, Otp input) {
            otpSource.findOtpByUserId(userId)
                    .effect(otpToBeValidated -> otpToBeValidated.validate(input))
                    .effect(otpToBeValidated -> otpToBeValidated.publishValidationResultTo(forum))
                    .failureEffect(forEach(forum::publish));
        }

        Workflow(OtpSource otpSource, Forum forum) {
            this.forum = forum;
            this.otpSource = otpSource;
        }
    }

    static class OtpSource {

        private final OtpRepository otpRepository;

        Result<OtpToBeValidated, OurFault> findOtpByUserId(UserId userId) {
            return this.otpRepository.findByUserId(userId)
                    .map(OtpToBeValidated::new)
                    .map(Result::<OtpToBeValidated, OurFault>success)
                    .orElseGet(() -> Result.failure(otpNotFound(userId)));
        }

        private static OurFault otpNotFound(UserId userId) {
            return OurFault.of(
                    ProblemDiagnosis.of(UseCaseProblem.OTP_NOT_FOUND)
                            .withContext("userId", userId.asLiteral())
            );
        }

        public OtpSource(OtpRepository otpRepository) {
            this.otpRepository = otpRepository;
        }
    }

    static class OtpToBeValidated {

        private final Otp otpOfUser;
        private OtpValidationResult validationResult;

        void validate(Otp input) {
            validationResult = otpOfUser.validate(input);
        }

        void publishValidationResultTo(Notifiable receiver) {
            receiver.on(OtpValidated.of(validationResult));
        }

        public OtpToBeValidated(Otp otpOfUser) {
            this.otpOfUser = otpOfUser;
        }
    }

    static class UserInterface implements Notifiable {

        final Client client;
        final OtpValidationPresenter presenter;

        @Override
        public void on(Object notification) {
            Match(notification).of(
                    VoidMatch.Case($(instanceOf(OtpValidated.class)), validated -> presenter.present(client, validated.document())),
                    DefaultIgnore()
            );
        }

        UserInterface(Client client, OtpValidationPresenter presenter) {
            this.client = client;
            this.presenter = presenter;
        }
    }

    public static class OutboundPorts {

        private final OtpRepository otpRepository;

        public OutboundPorts(OtpRepository otpRepository) {
            this.otpRepository = otpRepository;
        }
    }
}
