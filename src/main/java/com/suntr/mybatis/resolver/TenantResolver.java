package com.suntr.mybatis.resolver;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface TenantResolver {

    ThreadLocal<Tenant> TENANT = new InheritableThreadLocal<>();

    Tenant resolveTenant(ServletRequest request, ServletResponse response);

    default void setTenant(Tenant tenant) {
        TENANT.set(tenant);
    }

    default Tenant getTenant() {
        return TENANT.get();
    }

    default void clear() {
        TENANT.remove();
    }

}
