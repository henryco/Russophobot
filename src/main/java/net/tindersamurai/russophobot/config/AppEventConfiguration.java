package net.tindersamurai.russophobot.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.support.TaskUtils;

@Configuration
public class AppEventConfiguration {

	@Bean
	public ApplicationEventMulticaster applicationEventMulticaster() {
		val eventMulticaster = new SimpleApplicationEventMulticaster(); {
			eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
			eventMulticaster.setErrorHandler(TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER);
		}
		return eventMulticaster;
	}

}