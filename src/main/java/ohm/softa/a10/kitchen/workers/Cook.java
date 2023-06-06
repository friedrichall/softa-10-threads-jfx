package ohm.softa.a10.kitchen.workers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatchImpl;
import ohm.softa.a10.model.Dish;

public class Cook implements Runnable{
	private final String name;
	private final ProgressReporter progressReporter;
	private final KitchenHatchImpl kitchenHatch;
	@FXML
	ProgressBar orderQueueProgress;
	public Cook(String _name, ProgressReporter _progressReporter, KitchenHatchImpl _kitchenHatch){
		this.name = _name;
		this.progressReporter = _progressReporter;
		this.kitchenHatch = _kitchenHatch;
	}
	@Override
	public void run() {
		while(kitchenHatch.getOrderCount() > 0){
			Dish d = new Dish(kitchenHatch.dequeueOrder().getMealName());
			try {
				Thread.sleep(d.getCookingTime());
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			this.progressReporter.updateProgress();
			this.orderQueueProgress.setProgress((double) kitchenHatch.getOrderCount() / 100);
		}
	}
}
