package com.wangdong.lucene.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发模拟
 * 所有线程都在等待出发 底层for一直 直到countDown  更改  volatile int state 状态
 * @author 汪冬
 * @Date 2018/1/3
 */
public class Simulation {
	private static Integer count=1;
	static final CountDownLatch begin = new CountDownLatch(count); //为0时开始执行
	static  final ExecutorService exec = Executors.newFixedThreadPool(9);
	public static void simulationLoad(List<Runnable> runnableList) throws InterruptedException {
		runnableList.stream().forEach(run ->{
			exec.submit(run);
		});
		System.out.println("开始执行");
		//释放 十次 线程执行
		for (int i = 0; i < count; i++) {
			begin.countDown();
		}
		exec.shutdown();
	}

	public static CountDownLatch getBegin(){
		return begin;
	}

	public static ExecutorService getExec() {
		return exec;
	}
}
