package com.lcass.util;

public class helper_thread extends Thread{
	private Encapsulated_method function;
	private int tickrate = 60;
	private boolean running = true;
	private boolean paused = false;
	public helper_thread(Encapsulated_method to_run,int tickrate){
		function = to_run;
		this.tickrate = tickrate;
	}
	public void run() {
		
		float delta = 0;
		long timetick = 1000000000 / tickrate;
		long curtime = System.nanoTime();
		long lastime = System.nanoTime();
		while (running) {
			curtime = System.nanoTime();
			delta = curtime - lastime;
			if (delta >= timetick) {
				
				
				lastime = System.nanoTime();
				if(!paused){
					tick();
				}
			}
			try {
				this.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void tick(){
		
		function.call();
	}
	public void kill(){
		running = false;
	}
	public void pause(boolean pause){
		paused = pause;
	}

}
