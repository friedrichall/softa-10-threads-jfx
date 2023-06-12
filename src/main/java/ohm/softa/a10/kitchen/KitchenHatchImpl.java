package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch{
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

		synchronized (dishes){
			while (dishes.size() == 0){
				try {
					dishes.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return dishes.pollFirst();
	}

	@Override
	public void enqueueDish(Dish d) {
		//if deque is full, adding to deque has to wait
		synchronized (dishes){
			while (getDishesCount() >= this.maxMeals){
				try {
					dishes.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			dishes.offerLast(d);
		}

	}

	@Override
	public int getDishesCount() {
		return this.dishes.size();
	}
}
