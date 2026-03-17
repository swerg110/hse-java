	package hse.java.lectures.lecture6.tasks.synchronizer;

import lombok.Getter;

public class StreamingMonitor
	{
		@Getter
		private final int[] sortedIds;
		@Getter
		private final int[] counts;
		@Getter
		private final int ticksPerWriter;
		@Getter
		private int currentIdx = 0;
		@Getter
		private int n;

		StreamingMonitor(int[] sortedIds, int ticksPerWriter)
		{
			this.sortedIds = sortedIds;
			this.ticksPerWriter = ticksPerWriter;
			this.counts = new int[sortedIds.length];
			this.n = sortedIds.length * ticksPerWriter;
		}

		public synchronized void await(int id) throws InterruptedException
		{
			while (n > 0 && sortedIds[currentIdx] != id) wait();
			while (n == 0) wait();
		}

		public synchronized void tickDone()
		{
			counts[currentIdx]++;
			n--;
			
			if (n == 0)
			{
				notifyAll();
			}
			else
			{
				int n = sortedIds.length;
				int next = (currentIdx + 1) % n;
				while (counts[next] >= ticksPerWriter) next = (next + 1) % n;
				currentIdx = next;
				notifyAll();
			}
		}

		public synchronized void waitAll() throws InterruptedException 
		{
			while (n > 0) wait();
		}
	}
