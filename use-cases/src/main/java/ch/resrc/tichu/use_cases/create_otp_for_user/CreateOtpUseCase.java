package ch.resrc.tichu.use_cases.create_otp_for_user;

import ch.resrc.tichu.capabilities.error_handling.ProblemDiagnosis;
import ch.resrc.tichu.capabilities.error_handling.faults.OurFault;
import ch.resrc.tichu.capabilities.events.*;
import ch.resrc.tichu.capabilities.functional.VoidMatch;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.IdSequence;
import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.events.OtpCreated;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.inbound.CreateOtp;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.outbound.OtpPresenter;
import ch.resrc.tichu.use_cases.support.habits.errorhandling.UseCaseProblem;
import ch.resrc.tichu.use_cases.support.habits.events.Forum;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;
import ch.resrc.tichu.use_cases.support.outbound_ports.persistence.*;

import java.time.Instant;

import static ch.resrc.tichu.capabilities.functional.ForEach.forEach;
import static ch.resrc.tichu.capabilities.functional.VoidMatch.DefaultIgnore;
import static ch.resrc.tichu.use_cases.support.habits.errorhandling.Blame.isClientFault;
import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public class CreateOtpUseCase implements CreateOtp {

    private final OutboundPorts ports;

    private CreateOtpUseCase(OutboundPorts ports) {
        this.ports = ports;
    }

    public static CreateOtp create(OutboundPorts outboundPorts) {
        return new CreateOtpUseCase(outboundPorts);
    }

    @Override
    public void invoke(Input input, OtpPresenter presenter) {
        UserSource userSource = new UserSource(ports.userRepository, ports.userIds);

        OtpSource otpSource = new OtpSource(ports.otpRepository);

        Forum forum = new Forum();
        UserInterface ui = new UserInterface(input.client(), presenter);
        forum.events().subscribe(ui);

        Workflow workflow = new Workflow(userSource, otpSource, forum);

        workflow.createOtpForUser(input.email());
    }

    static class Workflow {

        final Forum forum;
        final UserSource userSource;
        final OtpSource otpSource;

        void createOtpForUser(Email email) {
            userSource.findOrCreateUser(email)
                    .map(x -> new NewOtpForUser(otpSource, x.id(), Otp.next()))
                    .effect(NewOtpForUser::store)
                    .effect((NewOtpForUser x) -> x.publishCreationSuccessTo(forum))
                    .failureEffect(forEach(forum::publish));
        }

        Workflow(UserSource userSource, OtpSource otpSource, Forum forum) {
            this.forum = forum;
            this.userSource = userSource;
            this.otpSource = otpSource;
        }
    }

    static class UserSource extends Eventful implements EventForwarding {

        private final UserRepository userRepository;
        private final IdSequence<UserId> userIds;

        Result<User, OurFault> findOrCreateUser(Email email) {
            return findUserByEmail(email)
                    .failureEffect(this::publish)
                    .ifEmptySwitch(() -> createUser(email))
                    .failureEffect(this::publish);
        }

        private Result<User, OurFault> findUserByEmail(Email email) {
            return this.userRepository.findUserByEmail(email)
                    .map(Result::<User, OurFault>success)
                    .orElseGet(Result::empty);
        }

        private Result<User, OurFault> createUser(Email email) {
            return User.resultOf(userIds.nextId(), Name.of(email.asLiteral()), email, Instant.now())
                    .failureEffect(this::publish)
                    .effect(userRepository::store)
                    .mapErrors(error -> userNotFoundOrCreated(Name.of(email.asLiteral()), email));
        }

        private static OurFault userNotFoundOrCreated(Name name, Email email) {
            return OurFault.of(
                    ProblemDiagnosis.of(UseCaseProblem.USER_NOT_FOUND_OR_CREATED)
                            .withContext("name", name.asLiteral())
                            .withContext("email", email.asLiteral())
            );
        }

        public UserSource(UserRepository userRepository, IdSequence<UserId> userIds) {
            this.userRepository = userRepository;
            this.userIds = userIds;
        }
    }

    static class OtpSource {

        private final OtpRepository otpRepository;

        void storeOtpForUser(UserId userId, Otp otp) {
            this.otpRepository.store(userId, otp);
        }

        public OtpSource(OtpRepository otpRepository) {
            this.otpRepository = otpRepository;
        }
    }

    static class NewOtpForUser {

        final OtpSource otpSource;
        final UserId userId;
        final Otp otp;

        void store() {
            otpSource.storeOtpForUser(userId, otp);
        }

        void publishCreationSuccessTo(Notifiable receiver) {
            receiver.on(OtpCreated.of(otp));
        }

        NewOtpForUser(OtpSource otpSource, UserId userId, Otp otp) {
            this.otpSource = otpSource;
            this.userId = userId;
            this.otp = otp;
        }
    }

    static class UserInterface implements Notifiable {

        final Client client;
        final OtpPresenter presenter;

        @Override
        public void on(Object notification) {
            Match(notification).of(
                    VoidMatch.Case($(instanceOf(OtpCreated.class)), otp -> presenter.present(client, otp.document())),
                    VoidMatch.Case($(instanceOf(ValidationError.class)), validationError -> presenter.present(client, validationError)),
                    VoidMatch.Case($(isClientFault()), error -> presenter.presentBusinessError(client, error)),
                    DefaultIgnore()
            );
        }

        UserInterface(Client client, OtpPresenter presenter) {
            this.client = client;
            this.presenter = presenter;
        }
    }

    public static class OutboundPorts {

        private final UserRepository userRepository;
        private final IdSequence<UserId> userIds;
        private final OtpRepository otpRepository;

        public OutboundPorts(UserRepository userRepository, IdSequence<UserId> userIds, OtpRepository otpRepository) {
            this.userRepository = userRepository;
            this.userIds = userIds;
            this.otpRepository = otpRepository;
        }
    }
}
