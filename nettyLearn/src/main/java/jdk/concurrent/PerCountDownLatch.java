package jdk.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CountDownLatch
 * 共享锁 如果条件不够满足 就一直在全局node 里面等待  直到触发
 * <li>为了实现所有线程等待最终的某个条件触发<li/>
 * 类似的功能 例如 @see java.util.concurrent.CyclicBarrier
 * @author 汪冬
 * @Date 2018/1/26
 */
public class PerCountDownLatch {

	private static CountDownLatch countDownLatch=new CountDownLatch(1);
	private static  AtomicInteger atomicInteger=new AtomicInteger();
	public static void main(String[] args) {
          //countRelease();
		  timeRelease();

	}

	private static void countRelease(){
		for (int i=0;i<10;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						countDownLatch.await();
						TimeUnit.SECONDS.sleep(2);
						System.out.println("count"+atomicInteger.incrementAndGet());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		countDownLatch.countDown();
	}


	private static void timeRelease(){
		for (int i=0;i<10;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						boolean await = countDownLatch.await(10, TimeUnit.SECONDS);
						System.out.println(await);
						System.out.println("time"+atomicInteger.incrementAndGet());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
