package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Waiter implements Runnable{
	private static final Logger logger = LogManager.getLogger(Waiter.class);
	private static final Random rnd = new Random();
	private final String name;
	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;

	public Waiter(String _name, KitchenHatch _kitchenHatch, ProgressReporter _progressReporter) {
		this.name = _name;
		this.kitchenHatch = _kitchenHatch;
		this.progressReporter = _progressReporter;
	}

	@Override
	public void run() {
		Dish d;
		do {
			d = kitchenHatch.dequeueDish();
			if(d != null){
				logger.info("Waiter " + name + " picked up dish " + d.getMealName());
				try {
					Thread.sleep(rnd.nextInt(1001));
					logger.info("Waiter" + name + "served dish " + d.getMealName());
				} catch (InterruptedException e) {
					logger.error("Failed to serve dish", e);
				}
			}


			progressReporter.updateProgress();
		} while (d != null);
		progressReporter.notifyWaiterLeaving();
		logger.info("Seems there's nothing to do anymore - {} going home", name);
	}
}
