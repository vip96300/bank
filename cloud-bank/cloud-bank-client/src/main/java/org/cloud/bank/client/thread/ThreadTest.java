package org.cloud.bank.client.thread;

public class ThreadTest implements Runnable{

	@Override
	public void run() {
		System.out.println(Thread.currentThread()+"begin");
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread()+"end");
	}

}
