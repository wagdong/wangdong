package jdk.concurrent;

import java.util.concurrent.Semaphore;

/**
 * 信号量  @see java.util.concurrent.CountDownLatch
 * 一次只是允许设定的信号量执行 直到有通道被释放 等待的线程 才会获取到机会执行
 * 例如 五条跑道 都在执行操作 第六个人过来 就必必须等待 区别 CountDownLatch 全部阻塞 直到释放到0 然后全部执行 CyclicBarrier 当阻塞的线程达到设置的阀门 全部释放
 * @author 汪冬
 * @Date 2018/1/28
 */
public class PerSemaphore {
	 static Semaphore semaphore=new Semaphore(1,true);
	public static void main(String[] args) {

		for (int i=0;i<5;i++){
			final int finalI = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						semaphore.acquire();
						System.out.println(finalI);
						semaphore.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}

	}
}
