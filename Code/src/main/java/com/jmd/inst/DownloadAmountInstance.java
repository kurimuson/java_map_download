package com.jmd.inst;

import java.text.DecimalFormat;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class DownloadAmountInstance {

	private final DecimalFormat df1 = new DecimalFormat("#.#");
	private final String[] s = { "B", "KB", "MB", "GB" };
	private long lastAmount = 0L;
	private long currentAmount = 0L;

	public synchronized void add(long amount) {
		this.currentAmount += amount;
	}

	public void saveLast() {
		this.lastAmount = this.currentAmount;
	}

	public String getDiffValue() {
		long diff = this.currentAmount - this.lastAmount;
		double d = (double) diff;
		int i = 0;
		while (!(d < 1024.0)) {
			d = d / 1024.0;
			i++;
		}
		return df1.format(d) + s[i];
	}

	public void reset() {
		this.lastAmount = 0;
		this.currentAmount = 0;
	}

	public long getLastAmount() {
		return lastAmount;
	}

	public long getCurrentAmount() {
		return currentAmount;
	}

}
