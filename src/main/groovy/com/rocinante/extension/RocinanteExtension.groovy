package com.rocinante.extension

import com.rocinante.Rocinante
import com.rocinante.interceptor.RocinanteInterceptor
import groovy.json.JsonSlurper
import org.junit.runner.Description
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.*

import java.lang.reflect.Method

class RocinanteExtension extends AbstractAnnotationDrivenExtension<Rocinante> {

    private JsonSlurper slurper = new JsonSlurper()

    private Rocinante rocinante

    @Override
    void visitFieldAnnotation(Rocinante rocinante, FieldInfo field) {
        this.rocinante = rocinante
        build(field)
    }

    private void build(FieldInfo fieldInfo) {
        def file = new File(rocinante.directory())

        if (file.exists()) {
            def mappingDir = new File(file, rocinante.mappings())

            if (mappingDir.exists()) {
                def mappings = mappingDir.listFiles()
                mappings.each {
                    buildFeature(it, fieldInfo)
                }
            }
        }

        fieldInfo.parent.features[0].skipped = true
    }

    private buildFeature(File mapping, FieldInfo fieldInfo) {
        def spec = fieldInfo.parent
        def clazz = spec.reflection

        def json = slurper.parseText(mapping.text)

        clazz.getDeclaredMethods().each { Method method ->
            FeatureMetadata metadata = method.getAnnotation(FeatureMetadata.class)
            if (metadata) {
                method.accessible = true
                spec.addFeature(createFeature(json, method, fieldInfo))
            }
        }
    }

    private FeatureInfo createFeature(json, method, fieldInfo) {
        def specInfo = fieldInfo.parent
        def name = buildName(json.request.url)

        def description = Description.createSuiteDescription(name)

        FeatureInfo feature = new FeatureInfo(parent: specInfo,
                name: name,
                description: description)

        MethodInfo featureMethod = new MethodInfo(parent: specInfo,
                name: name,
                description: description,
                feature: feature,
                reflection: method,
                kind: MethodKind.FEATURE,
        );

        def tapesDir = new File(rocinante.directory(), rocinante.tapes())

        def responseFile = new File(tapesDir, json.response.bodyFileName)

        featureMethod.addInterceptor(new RocinanteInterceptor(fieldInfo, responseFile.text))

        feature.featureMethod = featureMethod

        return feature
    }

    private String buildName(String name) {
        def splitted = name.split('\\?')
        def slug = splitted[0]
        def result = slug.replaceAll('/', '-').replaceAll('\\.', '_') - '-'
        if (splitted.size() > 1) {
            def rest = splitted[1]
            def params = rest.split('&')
            def keys = params*.split('=')*.first()
            result << [' (', keys.join(','), ','].join()
        }
        result
    }
}
