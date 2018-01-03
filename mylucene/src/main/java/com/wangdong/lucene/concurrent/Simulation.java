package com.wangdong.lucene.concurrent;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发模拟
 * @author 汪冬
 * @Date 2018/1/3
 */
public class Simulation {

	public static void main(String[] args) {
		final CountDownLatch begin = new CountDownLatch(1); //为0时开始执行
		final ExecutorService exec = Executors.newFixedThreadPool(9);

		for (int i = 0; i < 9; i++) {
			final int NO = i + 1;
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						begin.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			exec.submit(runnable);
		}
		System.out.println("开始执行");
		begin.countDown();
		exec.shutdown();
	}
}
