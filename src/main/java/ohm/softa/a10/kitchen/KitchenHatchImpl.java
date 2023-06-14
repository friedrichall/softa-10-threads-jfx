package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch{
	private static final Logger logger = LogManager.getLogger(KitchenHatchImpl.class);
	private final Integer maxMeals;
	private final Deque<Order> orders;
	private final Deque<Dish> dishes;
	public KitchenHatchImpl (Integer _maxMeals, Deque<Order> _orders){
		this.maxMeals = _maxMeals;
		this.orders = _orders;
		dishes = new LinkedList<>();
	}

	@Override
	public int getMaxDishes() {
		return this.maxMeals;
	}

	@Override
	public synchronized Order dequeueOrder(long timeout){
		//TODO wait(timeout);
		return orders.pollFirst();
	}

	@Override
	public synchronized int getOrderCount() {
		return orders.size();
	}

	@Override
	public Dish dequeueDish(long timeout) {

		long currentTimeStamp = System.nanoTime();
		synchronized (dishes){
			while (dishes.size() == 0) {
				try {
					logger.info("Kitchen hatch is empty. I can wait");
					dishes.wait();
					//thread is being awakened by another thread with notify()/notifyAll() in method enqueueDish(Dish d)
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				/* break condition as a waiter is leaving if it is getting a null result and waited for a long time */
				if (timeout > 0 && dishes.size() == 0 && System.nanoTime() - currentTimeStamp > timeout * 1000) {
					logger.info("Kitchen hatch still empty. Going home now");
					/* notify all waiters to re-enable them */
					dishes.notifyAll();
					return null;
				}
			}
			Dish dish = dishes.pollFirst();
			logger.info("notifying waiting cooks");
			dishes.notifyAll();
			return dish;
		}
	}

	@Override
	public void enqueueDish(Dish d) {
		//if deque is full, adding to deque has to wait
		synchronized (dishes){
			while (getDishesCount() >= this.maxMeals){
				try {
					logger.info("Kitchen hatch is full, waiting...");
					dishes.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			dishes.offerLast(d);
			logger.info("notifying waiting waiters");
			dishes.notifyAll();
		}

	}

	@Override
	public int getDishesCount() {
		return this.dishes.size();
	}
}
