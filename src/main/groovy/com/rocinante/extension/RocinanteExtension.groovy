package com.rocinante.extension

import com.rocinante.Config
import com.rocinante.Rocinante
import com.rocinante.interceptor.RocinanteInterceptor
import groovy.json.JsonSlurper
import org.junit.runner.Description
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.*

import java.lang.reflect.Method

class RocinanteExtension extends AbstractAnnotationDrivenExtension<Rocinante> {

    private JsonSlurper slurper = new JsonSlurper()

    private Config config = new Config()

    private def bindings = []

    @Override
    void visitFieldAnnotation(Rocinante rocinante, FieldInfo field) {
        bindings << new Expando(rocinante: rocinante, field: field)
    }

    @Override
    void visitSpecAnnotation(Rocinante annotation, SpecInfo spec) {
        def file = new File(config.basepath)

        if (file.exists()) {
            def mappingDir = new File(file, config.mappings)

            if (mappingDir.exists()) {
                def mappings = mappingDir.listFiles()
                mappings.each {
                    buildFeature(it, spec)
                }
            }
        }

        spec.features[0].skipped = true
    }

    private buildFeature(File mappingFile, SpecInfo spec) {
        def clazz = spec.reflection

        def mapping = slurper.parseText(mappingFile.text)

        clazz.getDeclaredMethods().each { Method method ->
            FeatureMetadata metadata = method.getAnnotation(FeatureMetadata.class)
            if (metadata) {
                method.accessible = true
                spec.addFeature(createFeature(mapping, method, spec))
            }
        }
    }

    private FeatureInfo createFeature(mapping, method, spec) {
        def name = buildName(mapping.request.url)

        def description = Description.createSuiteDescription(name)

        FeatureInfo feature = new FeatureInfo(parent: spec,
                name: name,
                description: description)

        MethodInfo featureMethod = new MethodInfo(parent: spec,
                name: name,
                description: description,
                feature: feature,
                reflection: method,
                kind: MethodKind.FEATURE,
        );

        featureMethod.addInterceptor(new RocinanteInterceptor(mapping, bindings, config))

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
