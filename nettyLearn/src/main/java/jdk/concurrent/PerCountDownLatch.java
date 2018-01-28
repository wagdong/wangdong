package jdk.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对CountDownLatch的学习
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
						/**
						 * 可能是Aqs思想
						 * 源码分析总体思想是加入全局的node 链表里面where(true) 检测触发条件 state=0 或者 Time<=0
						 * 释放阻塞全部 线程
						 */
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
