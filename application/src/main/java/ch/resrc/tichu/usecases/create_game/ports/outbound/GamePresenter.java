package ch.resrc.tichu.usecases.create_game.ports.outbound;

import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;
import ch.resrc.tichu.usecases.create_game.ports.documents.GameDocument;

public interface GamePresenter extends ErrorPresenter {

  void present(GameDocument toBePresented);

}
