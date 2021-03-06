package net.oschina.j2cache.autoconfigure;

import java.io.IOException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2Cache;
import net.oschina.j2cache.J2CacheBuilder;
import net.oschina.j2cache.cache.support.util.SpringUtil;
/**
 * 启动入口
 * @author zhangsaizz
 *
 */
@ConditionalOnClass(J2Cache.class)
@EnableConfigurationProperties({J2CacheConfig.class})
@Configuration
public class J2CacheAutoConfiguration {
	
    private final J2CacheConfig j2CacheConfig;

    public J2CacheAutoConfiguration(J2CacheConfig j2CacheConfig) {
        this.j2CacheConfig = j2CacheConfig;
    }

    @Bean
    @DependsOn("springUtil")
    public CacheChannel cacheChannel() throws IOException {
    	net.oschina.j2cache.J2CacheConfig cacheConfig = new net.oschina.j2cache.J2CacheConfig();
    	cacheConfig = net.oschina.j2cache.J2CacheConfig.initFromConfig(j2CacheConfig.getConfigLocation());
    	J2CacheBuilder builder = J2CacheBuilder.init(cacheConfig);
        return builder.getChannel();
    }

    @Bean
    public SpringUtil springUtil() {
    	return new SpringUtil();
    }

}
