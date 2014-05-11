/**
 * Dianping.com Inc.
 * Copyright (c) 2003-2013 All Rights Reserved.
 */
package com.dianping.pigeon.remoting.provider.config;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.dianping.pigeon.remoting.common.util.Constants;

public class ProviderConfig<T> {

	private Class<T> serviceInterface;
	private String url;
	private String version;
	private T service;
	private ServerConfig serverConfig = new ServerConfig();
	private boolean published = false;
	private boolean cancelTimeout = Constants.DEFAULT_TIMEOUT_CANCEL;

	public boolean isCancelTimeout() {
		return cancelTimeout;
	}

	public void setCancelTimeout(boolean cancelTimeout) {
		this.cancelTimeout = cancelTimeout;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(ServerConfig serverConfig) {
		if (serverConfig != null) {
			this.serverConfig = serverConfig;
		}
	}

	public ProviderConfig(Class<T> serviceInterface, T service) {
		if (!serviceInterface.isInstance(service)) {
			throw new IllegalArgumentException("Service interface [" + serviceInterface.getName()
					+ "] needs to be implemented by service [" + service + "] of class ["
					+ service.getClass().getName() + "]");
		}
		this.setServiceInterface(serviceInterface);
		this.setService(service);
	}

	public ProviderConfig(T service) {
		Class<?>[] interfaces = service.getClass().getInterfaces();
		Class interfaceClass = null;
		if (interfaces != null && interfaces.length > 0) {
			interfaceClass = service.getClass().getInterfaces()[0];
		} else {
			interfaceClass = service.getClass();
		}
		this.setService(service);
		this.setServiceInterface(interfaceClass);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Class<T> getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class<T> serviceInterface) {
		/*
		 * if (serviceInterface != null && !serviceInterface.isInterface()) {
		 * throw new
		 * IllegalArgumentException("'serviceInterface' must be an interface");
		 * }
		 */
		this.serviceInterface = serviceInterface;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		if (url != null) {
			url = url.trim();
		}
		this.url = url;
	}

	public T getService() {
		return service;
	}

	public void setService(T service) {
		this.service = service;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
