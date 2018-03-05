package org.cloud.bank.client.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	/**
	 * 线程池的基本大小
	 */
	private static final int corePoolSize=100;
	/**
	 * 线程池最大大小
	 */
	private static final int maximumPoolSize=1000;
	/**
	 * 线程活动保持时间
	 */
	private static final long keepAliveTime=10;
	/**
	 * 线程池队列数
	 */
	private static final int capacity=50;
	/**
	 * 获取线程执行者
	 * @return
	 */
	private static ThreadPoolExecutor threadPoolExecutor;
	
	public static ThreadPoolExecutor getThreadPoolExecutor(){
		if(threadPoolExecutor==null){
			return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(capacity),new ThreadPoolExecutor.DiscardOldestPolicy());
		}
		return threadPoolExecutor;
	}
	
	/*public static void main(String []orgs){
		for(int i=0;i<1000;i++){
			ThreadPool.getThreadPool().execute(new ThreadTest());
		}
		
	}*/
}
