package com.rocinante.interceptor

import com.rocinante.Config
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo

/**
 *
 */
class RocinanteInterceptor extends AbstractMethodInterceptor {
    private final def mapping

    private List bindings

    Config config

    RocinanteInterceptor(mapping, bindings, config) {
        this.mapping = mapping
        this.bindings = bindings
        this.config = config
    }

    @Override
    public void interceptFeatureMethod(IMethodInvocation invocation) throws Throwable {
        println ">>> $bindings"
        bindings.each {
            String configValue  = findValue(config, it.rocinante.config()).last()
            String mappingValue = findValue(mapping, it.rocinante.binding()).last()
            FieldInfo field = it.field
            if (it.rocinante.content()) {
                // TODO: create a context for this
                File tapesDir = new File(config.basepath, config.tapes)
                File responseFile = new File(tapesDir, mappingValue)
                mappingValue = responseFile.text
            }
            field.writeValue(invocation.target, configValue ?: mappingValue)
        }

        invocation.proceed()
    }

    private List findValue(mapping, value, composition = []) {
        def splitted = value.split('\\.')
        def newValue = mapping[splitted[0]]
        composition << newValue
        if (splitted.size() > 1) findValue(newValue, splitted[1], composition)
        else composition
    }
}