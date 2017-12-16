package media.thehoard.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
	public static final ExecutorService PRIMARY_THREAD_POOL = Executors.newCachedThreadPool();
}
