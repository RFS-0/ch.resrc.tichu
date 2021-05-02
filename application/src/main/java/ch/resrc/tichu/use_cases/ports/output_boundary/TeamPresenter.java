package ch.resrc.tichu.use_cases.ports.output_boundary;

import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.common_ports.output.TeamDocument;

public interface TeamPresenter extends ErrorPresenter {

  void present(Id receiver, TeamDocument toBePresented);

}
