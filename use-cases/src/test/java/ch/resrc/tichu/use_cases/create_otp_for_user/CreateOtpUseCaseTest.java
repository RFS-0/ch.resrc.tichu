package ch.resrc.tichu.use_cases.create_otp_for_user;

import ch.resrc.tichu.domain.entities.UserId;
import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.test.capabilities.adapters.testdoubles.TestOtpPresenter;
import ch.resrc.tichu.test.capabilities.habits.fixtures.*;
import ch.resrc.tichu.test.capabilities.testbed.TestBed;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;
import org.junit.jupiter.api.*;

import static ch.resrc.tichu.test.capabilities.habits.use_cases.CreateOtpInputSpec.createOtpInput;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateOtpUseCaseTest
        implements

        ValueObjectHabits,

        UserFixtureHabits {

    @Nested
    class If_requested_to_create_a_new_otp {

        @Test
        void it_creates_a_new_user_if_none_exists_for_email() {
            new TestBed.Context() {{
                // given:
                var input = createOtpInput()
                        .client(Client.of(ClientId.of("d4524bfd-d370-45c1-b940-5f0f50a960d7")))
                        .email(Email.of("yara@wallis.xyz"))
                        .asInput();
                var useCase = newCreateOtp();
                var presenter = new TestOtpPresenter();

                // when:
                useCase.invoke(input, presenter);

                // then
                assertThat(ports().userRepository().findAll().size()).isEqualTo(1);
                var user = ports().userRepository().findUserByEmail(
                        Email.of("yara@wallis.xyz")
                );

                assertThat(user.isPresent()).isTrue();
                assertThat(user.get().name()).isEqualTo(Name.of("yara@wallis.xyz"));
                assertThat(user.get().email()).isEqualTo(Email.of("yara@wallis.xyz"));
            }};
        }

        @Test
        void it_uses_the_existing_if_one_exists_for_email() {
            new TestBed.Context() {{
                // given:
                var user = aUser()
                        .id(aUserId("0a2e1b6b-c070-4d12-9868-fd476c195d39"))
                        .email(email("lucien@gough.xyz"))
                        .asEntity();
                ports().userRepository().store(user);

                var input = createOtpInput()
                        .client(Client.of(ClientId.of("fc8206c1-a20e-4439-a9ff-ca2a8b464f4c")))
                        .email(Email.of("lucien@gough.xyz"))
                        .asInput();
                var useCase = newCreateOtp();
                var presenter = new TestOtpPresenter();

                // when:
                useCase.invoke(input, presenter);

                // then
                assertThat(ports().userRepository().findAll().size()).isEqualTo(1);
                var otp = ports().otpRepository().findByUserId(UserId.of("0a2e1b6b-c070-4d12-9868-fd476c195d39"));
                assertThat(otp.isPresent()).isTrue();
                assertThat(otp.get()).isNotNull();
            }};
        }

        @Test
        void it_stores_a_new_otp() {
            new TestBed.Context() {{
                // given:
                var user = aUser()
                        .id(aUserId("0a2e1b6b-c070-4d12-9868-fd476c195d39"))
                        .email(email("stefan@duerr.xyz"))
                        .asEntity();
                ports().userRepository().store(user);

                var input = createOtpInput()
                        .client(Client.of(ClientId.of("9e986fec-d5e8-49c3-853d-269065abca2d")))
                        .email(Email.of("stefan@duerr.xyz"))
                        .asInput();
                var useCase = newCreateOtp();
                var presenter = new TestOtpPresenter();

                // when:
                useCase.invoke(input, presenter);

                // then
                var otp = ports().otpRepository().findByUserId(UserId.of("0a2e1b6b-c070-4d12-9868-fd476c195d39"));
                assertThat(otp.isPresent()).isTrue();
                assertThat(otp.get()).isNotNull();
            }};
        }

        @Test
        void it_presents_the_new_otp() {
            new TestBed.Context() {{
                // given:
                var user = aUser()
                        .id(aUserId("8882be8e-af1e-4c42-b33c-f1f41edab5a0"))
                        .email(email("lucien@gough.xyz"))
                        .asEntity();
                ports().userRepository().store(user);

                var input = createOtpInput()
                        .client(Client.of(ClientId.of("7d5e171f-5794-47ed-8d51-ebf12ecb254a")))
                        .email(Email.of("lucien@gough.xyz"))
                        .asInput();
                var useCase = newCreateOtp();
                var presenter = new TestOtpPresenter();

                // when:
                useCase.invoke(input, presenter);

                // then
                var otp = ports().otpRepository().findByUserId(UserId.of("8882be8e-af1e-4c42-b33c-f1f41edab5a0"));
                assertThat(otp.isPresent()).isTrue();
                assertThat(otp.get()).isNotNull();

                var presented = presenter.presented();
                assertThat(presented).isNotNull();
                assertThat(presented.oneTimePassword().value()).isEqualTo(otp.get().value());
            }};
        }
    }
}