package com.rocinante.interceptor

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo

/**
 *
 */
class RocinanteInterceptor extends AbstractMethodInterceptor {
    private final FieldInfo fieldInfo
    private final String url

    RocinanteInterceptor(FieldInfo fieldInfo, String url) {
        this.fieldInfo = fieldInfo
        this.url = url
    }

    @Override
    public void interceptFeatureMethod(IMethodInvocation invocation) throws Throwable {
        fieldInfo.writeValue(invocation.target, url)
        invocation.proceed()
    }
}