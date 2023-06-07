package ohm.softa.a10.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.kitchen.KitchenHatchImpl;
import ohm.softa.a10.kitchen.workers.Cook;
import ohm.softa.a10.kitchen.workers.Waiter;
import ohm.softa.a10.model.Order;
import ohm.softa.a10.util.NameGenerator;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import static ohm.softa.a10.KitchenHatchConstants.*;

public class MainController implements Initializable {

	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;
	private final NameGenerator nameGenerator;

	@FXML
	private ProgressIndicator waitersBusyIndicator;

	@FXML
	private ProgressIndicator cooksBusyIndicator;

	@FXML
	private ProgressBar kitchenHatchProgress;

	@FXML
	private ProgressBar orderQueueProgress;

	public MainController() {
		nameGenerator = new NameGenerator();

		final var orders = new LinkedList<Order>();
		//offerLast() returns true if adding was successfully
		/*this.orders.offerLast( new Order("vegane Fettbämme"));
		this.orders.offerLast( new Order("Trüffelkuchen"));
		this.orders.offerLast( new Order("Schoko-Lava-Cake"));*/

		IntStream.range(0, ORDER_COUNT)
			.mapToObj(i -> new Order(nameGenerator.getRandomDish()))
			.forEach(orders::offerLast);

		this.kitchenHatch = new KitchenHatchImpl(KITCHEN_HATCH_SIZE, orders);
		this.progressReporter = new ProgressReporter(kitchenHatch, COOKS_COUNT, WAITERS_COUNT, ORDER_COUNT, KITCHEN_HATCH_SIZE);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderQueueProgress.progressProperty().bindBidirectional(this.progressReporter.orderQueueProgressProperty());
		kitchenHatchProgress.progressProperty().bindBidirectional(this.progressReporter.kitchenHatchProgressProperty());
		waitersBusyIndicator.progressProperty().bindBidirectional(this.progressReporter.waitersBusyProperty());
		cooksBusyIndicator.progressProperty().bind(this.progressReporter.cooksBusyProperty());

		/* TODO create the cooks and waiters, pass the kitchen hatch and the reporter instance and start them */
		for (int i = 0; i < COOKS_COUNT; i++){
			Thread thread = new Thread(new Cook(nameGenerator.generateName(), this.kitchenHatch, this.progressReporter));
			thread.start();
		}
		for (int i = 0; i < WAITERS_COUNT; i++){
			Thread thread = new Thread(new Waiter(nameGenerator.generateName(), this.kitchenHatch, this.progressReporter));
			thread.start();
		}
	}
}
