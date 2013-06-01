package com.c2.sound;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;

import com.c2.leap.LeapParameterListener;
import com.c2.leap.LeapParameters;

import de.sciss.jcollider.JCollider;
import de.sciss.jcollider.Server;

public class SoundGenerator implements LeapParameterListener {
	
	Server server;
	boolean receivingParameters = false;

	public void start() {
/*		// Start the SuperCollider server
		CommandLine scCommand = new CommandLine("scsynth");
		scCommand.addArgument("-t").addArgument("57110");
		ExecuteResultHandler erh = new DefaultExecuteResultHandler();
		Executor executor = new DefaultExecutor();
		// Prepare to kill it when the program exits
		killOnShutdown(executor);
		// Actually start it
		try {
			executor.execute(scCommand, erh);
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
			onLeapParametersChanged(null);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			server = new Server("SoundGenerator");
			server.start();
			server.boot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void killOnShutdown(final Executor executor) {
		ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		executor.setWatchdog(watchdog);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				executor.getWatchdog().destroyProcess();
			}
		});
	}
	
	public void onFirstLeapParameters(LeapParameters newParameters) {
		
	}

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		if (!receivingParameters) {
			receivingParameters = true;
			onFirstLeapParameters(newParameters);
		} else {
			// Do OnChanged stuff
		}
	}

}
