package store.controller;

import store.model.Inventory;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final Inventory inventory;

    public StoreController(InputView inputView, OutputView outputView, Inventory inventory) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inventory = inventory;
    }

    public void startStore() {

    }
}
