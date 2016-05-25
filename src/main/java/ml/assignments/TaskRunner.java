package ml.assignments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskRunner {

	private List<? extends Task> tasks;
	private List<Object> results = new CopyOnWriteArrayList<>();
	private List<Future<Object>> futures = new ArrayList<>();

	public TaskRunner(List<? extends Task> tasks) {
		super();
		this.tasks = tasks;
	}

	public List<Object> run() {
		ExecutorService service = Executors.newFixedThreadPool(tasks.size());
		for (final Task task : tasks) {
			Callable<Object> callable = new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					Object o = task.execute();
					results.add(o);
					return o;
				}
			};
			futures.add(service.submit(callable));
		}
		service.shutdown();
		for (Future<Object> future : futures) {
			try {
				results.add(future.get());
			} catch (Exception e) {
				throw new RuntimeException("Error gettting Future result", e);
			}
		}
		return results;
	}
}
