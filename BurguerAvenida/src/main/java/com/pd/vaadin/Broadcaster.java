package com.pd.vaadin;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broadcaster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3930793370328460268L;
	
	public static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(/*Set<OrderLine> lines*/String msg);
	}

	private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();

	public static synchronized void register(BroadcastListener listener) {
		listeners.add(listener);
	}

	public static synchronized void unregister(BroadcastListener listener) {
		listeners.remove(listener);
	}

	public static synchronized void broadcast(/*final Set<OrderLine> lines*/String msg) {
		for (final BroadcastListener listener : listeners)
			executorService.execute(()-> listener.receiveBroadcast(msg));
	}
}