package co.edu.uptc.presenter;

import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;
import co.edu.uptc.model.ModelImplement;
import co.edu.uptc.view.ConsoleView;

public class Runner {
    private ModelInterface model;
    private ViewInterface view;
    private PresenterInterface presenter;

    public void makeMVP() {
        model = new ModelImplement();
        view = new ConsoleView();
        presenter = new MainPresenter();

        presenter.setModel(model);
        presenter.setView(view);
        view.setPresenter(presenter);
    }

    public void start() {
        makeMVP();
        view.start();
    }
}
