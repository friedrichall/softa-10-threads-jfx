package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.Deque;

public class KitchenHatchImpl implements KitchenHatch{
	private Integer maxMeals;
	private Deque<Order> orders;
	private Deque<Dish> dishes;
	public KitchenHatchImpl (Integer _maxMeals, Deque<Order> _orders){
		this.maxMeals = _maxMeals;
		this.orders = _orders;
	}

	@Override
	public int getMaxDishes() {
		return this.maxMeals;
	}

	@Override
	public Order dequeueOrder(long timeout) {
		//TODO wait(timeout);
		return orders.pollFirst();
	}

	@Override
	public int getOrderCount() {
		return orders.size();
	}

	@Override
	public Dish dequeueDish(long timeout) {
		return dishes.pollFirst();
	}

	@Override
	public void enqueueDish(Dish m) {
		//if deque is full, adding to deque has to wait
		dishes.offerLast(m);
	}

	@Override
	public int getDishesCount() {
		return 0;
	}
}
