package hse.java.lectures.lecture6.tasks.queue;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBlockingQueue<T> {

	private final int capacity;
	private Queue<T> queue = new LinkedList<T>();

	public BoundedBlockingQueue(int capacity) {
		if (capacity <= 0){
			throw new IllegalArgumentException();
		}
		this.capacity = capacity;
	}

	public synchronized void put(T item) throws InterruptedException {
		if (item == null){
			throw  new IllegalArgumentException();
		}

		while (queue.size() == capacity) wait();
		queue.offer(item);
		notifyAll();
	}

	public synchronized T take() throws InterruptedException {
		while(queue.isEmpty()) wait();
		T item = queue.poll();
		notifyAll();
		return item;
	}

	public synchronized int size() {
		return queue.size();
	}

	public synchronized int capacity() {
		return capacity;
	}
}
