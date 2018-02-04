import org.junit.Test;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * @author 汪冬
 * @Date 2018/2/4
 */
public class MainTest {

	private static boolean key=true;

	public static void main(String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i=0;
				while (key){
					System.out.println(i++);
				}
			}
		}).start();
		TimeUnit.SECONDS.sleep(10);
		key=false;
	}

}
