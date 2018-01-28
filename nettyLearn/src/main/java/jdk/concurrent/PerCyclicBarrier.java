package jdk.concurrent;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对CyclicBarrier的学习
 * 源码简单分析
 * 1.总体上 是通过reentLock wait 和 notifAll 思想进行等到同步能执行   底层是也是for(;;) 循环进行处理 不断监听判断
 * @author 汪冬
 * @Date 2018/1/28
 */
public class PerCyclicBarrier {

	 CyclicBarrier cyclicBarrier = new CyclicBarrier(parties);
	 CyclicBarrier cyclicBarrierRunable = new CyclicBarrier(parties, new Runnable() {
		 @Override
		 public void run() {
			 System.out.println("runable done!");
		 }
	 });
	AtomicInteger atomicInteger=new AtomicInteger();
	static int parties=4;
	public static void main(String[] args) throws Exception {
           // new PerCyclicBarrier().cyclicBarrier();
            new PerCyclicBarrier().cyclicBarrierRunable();

	}


	private  void  cyclicBarrier() {
		for (int i=0;i<4;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						int increment = atomicInteger.getAndIncrement();
						System.out.println(increment+"wait!!!!");
						cyclicBarrier.await();
						System.out.println(increment+"done!");
					}catch (Exception e){

					}
				}
			}).start();
		}

	}

	private void cyclicBarrierRunable(){
		for (int i=0;i<4;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						int increment = atomicInteger.getAndIncrement();
						System.out.println(increment+"wait!!!!");
						cyclicBarrierRunable.await();
						System.out.println(increment+"done!");
					}catch (Exception e){

					}
				}
			}).start();
		}
	}




}
