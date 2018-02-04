package jdk.concurrent;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 独占锁
 * 在双向fifo 队列中 会有一个thread 独占，其他的thread 将等待 独占thread 释放
 * for(;;) 会一直循环 判断 当前独占锁是否释放（unlock set state-1）
 * 当前tReentrantLock 可以lock set state+1
 * Created by SY on 2017/3/30.
 */
public class PerReentrantLock {

	//ip对应的条件锁
	private static Map<String, ReentrantLock> reentrantLockMap = new ConcurrentHashMap<String,ReentrantLock>();

	private static Map<Integer, String> ipMap = new HashMap<Integer, String>();

	static {
		ipMap.put(0, "192.168.10.86");
		ipMap.put(1, "192.168.10.87");
		ipMap.put(2, "192.168.10.88");
		ipMap.put(3, "192.168.10.89");
		ipMap.put(4, "192.168.10.90");
	}


	private static Executor executor = new ThreadPoolExecutor(8, 16, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue());


	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			final int finalI = i;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					int index = (int) (Math.random() * 5);
					String s = ipMap.get(index);
					if (reentrantLockMap.get(s) == null) {
						ReentrantLock myLock = new ReentrantLock();
						reentrantLockMap.put(s, myLock);
					}
					ReentrantLock reentrantLock = reentrantLockMap.get(s);
					try {
						reentrantLock.lock();
							System.out.println(s + "获得当前锁" + finalI);
							TimeUnit.SECONDS.sleep(20);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						//释放
						System.out.println(s + "释放当前锁" + finalI);
						reentrantLock.unlock();
					}
				}
			});
		}
	}


	@Test
	public void test(){
		System.out.println(new Date(Long.parseLong("1514502002000")));
	}

}
