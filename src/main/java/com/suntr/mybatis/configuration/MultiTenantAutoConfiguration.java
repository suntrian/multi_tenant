package com.suntr.mybatis.configuration;

import com.suntr.mybatis.filter.TenantWebFilter;
import com.suntr.mybatis.resolver.TenantResolver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.annotation.PostConstruct;
import java.util.List;

@EnableConfigurationProperties(MultiTenantProperties.class)
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@Configuration
public class MultiTenantAutoConfiguration {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactories;

    @Autowired
    private MultiTenantProperties multiTenantProperties;

    @Autowired(required = false)
    private InterceptorProperties interceptorProperties;

    @Bean
    @ConditionalOnMissingBean(InterceptorProperties.class)
    public InterceptorProperties interceptorProperties() {
        return null;
    }

    @PostConstruct
    public void multiTenantInterceptor() {
        MultiTenantInterceptor interceptor = new MultiTenantInterceptor();
        interceptor.setProperties(multiTenantProperties.getProperties());
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnBean(TenantResolver.class)
    public FilterRegistrationBean<TenantWebFilter> tenantWebFilter(TenantResolver tenantResolver) {
        TenantWebFilter filter = new TenantWebFilter(tenantResolver);
        FilterRegistrationBean<TenantWebFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/**");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("tenantWebFilter");
        registration.setOrder(1);
        return registration;
    }

}
