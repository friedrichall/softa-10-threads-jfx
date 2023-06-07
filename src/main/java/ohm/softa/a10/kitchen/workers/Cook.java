package ohm.softa.a10.kitchen.workers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cook implements Runnable{
	private static final Logger logger = LogManager.getLogger(Cook.class);
	private final String name;
	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;
	@FXML
	ProgressBar orderQueueProgress;
	public Cook(String _name, KitchenHatch _kitchenHatch, ProgressReporter _progressReporter){
		this.name = _name;
		this.progressReporter = _progressReporter;
		this.kitchenHatch = _kitchenHatch;
	}
	@Override
	public void run() {
		Order o;
		do {
			o = kitchenHatch.dequeueOrder();
			if (o != null){
				Dish d = new Dish(o.getMealName());
				try {
					Thread.sleep(d.getCookingTime());
					logger.info("Cook " + name + " prepared meal " + d.getMealName());
				} catch (InterruptedException e) {
					logger.error("Failed to cook meal", e);
				}

				kitchenHatch.enqueueDish(d);
				logger.info("Cook " + name + " put meal " + d.getMealName() + " into the kitchen hatch.");

				this.progressReporter.updateProgress();
			}
		} while (o != null);
		progressReporter.notifyCookLeaving();
	}
}
