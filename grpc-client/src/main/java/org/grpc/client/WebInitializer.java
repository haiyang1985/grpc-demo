package org.grpc.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

// 默认只会Component Scan com.ctrip.framework.benchmark.service以及其子package，如果需要Scan更多的package可以使用@SpringBootApplication(scanBasePackages = {"com.ctrip.framework.benchmark.service", "other package"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WebInitializer extends SpringBootServletInitializer {

  /**
   * Configure your application when it’s launched by the servlet container
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    System.setProperty("cat.client.queueSize", "10000");
    return application.sources(WebInitializer.class);
  }
}
