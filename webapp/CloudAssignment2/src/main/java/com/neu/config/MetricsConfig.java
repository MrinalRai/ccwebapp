package com.neu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.neu.controller.UserController;
import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Configuration
public class MetricsConfig {
	@Value("${publish.metrics}")
	private boolean publishMetrics;

	@Value("${metrics.server.hostname}")
	private String metricsServerHost;

	@Value("${metrics.server.port}")
	private int metricsServerPort;
	
	private final static Logger logger = LoggerFactory.getLogger(MetricsConfig.class);

	@Bean
	public StatsDClient metricsClient() {

		if (publishMetrics) {
			logger.debug("Inside metrics client");
			return new NonBlockingStatsDClient("csye6225", metricsServerHost, metricsServerPort);
		}

		return new NoOpStatsDClient();
	}

}
