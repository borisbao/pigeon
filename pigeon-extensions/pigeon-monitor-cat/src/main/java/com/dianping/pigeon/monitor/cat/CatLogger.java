/**
 * Dianping.com Inc.
 * Copyright (c) 2003-2013 All Rights Reserved.
 */
package com.dianping.pigeon.monitor.cat;

import org.apache.log4j.Logger;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.MessageProducer;
import com.dianping.cat.message.Transaction;
import com.dianping.pigeon.component.invocation.InvocationContext;
import com.dianping.pigeon.monitor.MonitorLogger;
import com.dianping.pigeon.monitor.MonitorTransaction;

/**
 * @author xiangwu
 * @Oct 8, 2013
 * 
 */
public class CatLogger implements MonitorLogger {

	private static final Logger logger = Logger.getLogger(CatLogger.class);
	private volatile long errorCounter = 0L;
	private MessageProducer producer = null;

	public CatLogger(MessageProducer producer) {
		this.producer = producer;
	}

	public CatLogger() {
	}

	public MessageProducer getMessageProducer() {
		return producer;
	}

	@Override
	public MonitorTransaction createTransaction(String name, String uri, InvocationContext invocationContext) {
		if (producer != null) {
			Transaction transaction = producer.newTransaction(name, uri);
			return new CatMonitorTransaction(this, transaction, invocationContext);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dianping.pigeon.monitor.MonitorLogger#logError(java.lang.Throwable)
	 */
	@Override
	public void logError(Throwable t) {
		try {
			if (producer == null) {
				producer = Cat.getProducer();
			}
			if (producer != null && t != null) {
				producer.logError(t);
			}
		} catch (Exception e) {
			logMonitorError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dianping.pigeon.monitor.MonitorLogger#logEvent(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void logEvent(String name, String source, String event) {
		try {
			if (producer == null) {
				producer = Cat.getProducer();
			}
			if (producer != null) {
				producer.logEvent(name, source, Event.SUCCESS, event);
			}
		} catch (Exception e) {
			logMonitorError(e);
		}
	}

	@Override
	public void logMonitorError(Throwable t) {
		try {
			String errorMsg = "[Cat]Monitor pigeon call failed.";
			if (errorCounter <= 50) {
				logger.error(errorMsg, t);
			} else if (errorCounter < 1000 && errorCounter % 40 == 0) {
				logger.error(errorMsg, t);
			} else if (errorCounter % 200 == 0) {
				logger.error(errorMsg, t);
			}
		} catch (Exception e2) {/* do nothing */
		}
		errorCounter++;
	}

}
