package hse.java.lectures.lecture6.tasks.synchronizer;

import lombok.Getter;

import java.io.PrintStream;

public class StreamWriter implements Runnable
{
	@Getter
	private final String message;
	@Getter
	private final int id;
	@Getter
	private final PrintStream output;
	@Getter
	private final Runnable onTick;
	@Getter
	private volatile StreamingMonitor monitor;

	public StreamWriter(int id, String message, PrintStream output, Runnable onTick) {
		this.message = message;
		this.id = id;
		this.output = output;
		this.onTick = onTick;
	}

	public void attach(StreamingMonitor monitor) 
	{
		this.monitor = monitor;
	}

	@Override
	public void run() 
	{
		while (true) 
		{
			try 
			{
				monitor.await(id);
				output.print(message);
				onTick.run();
				monitor.tickDone();
			} catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
