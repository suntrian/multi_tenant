package com.suntr.mybatis.filter;

import com.suntr.mybatis.resolver.Tenant;
import com.suntr.mybatis.resolver.TenantResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TenantWebFilter extends OncePerRequestFilter {

    TenantResolver resolver;

    public TenantWebFilter(TenantResolver resolver) {
        this.resolver = resolver;
    }

    public void setResolver(TenantResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Tenant tenant = resolver.resolveTenant(httpServletRequest, httpServletResponse);
        resolver.setTenant(tenant);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
