package com.rocinante.extension

import com.rocinante.Config
import com.rocinante.Rocinante
import com.rocinante.interceptor.RocinanteInterceptor
import groovy.json.JsonSlurper
import groovy.util.logging.Log
import org.junit.runner.Description
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.*

import java.lang.reflect.Method

@Log
class RocinanteExtension extends AbstractAnnotationDrivenExtension<Rocinante> {

    private JsonSlurper slurper = new JsonSlurper()

    private Config config = Config.instance

    private def bindings = []

    @Override
    void visitFieldAnnotation(Rocinante rocinante, FieldInfo field) {
        bindings << new Expando(rocinante: rocinante, field: field)
    }

    @Override
    void visitSpecAnnotation(Rocinante rocinante, SpecInfo spec) {
        def file = new File(config.basepath)

        if (file.exists()) {
            def mappingDir = new File(file, config.mappings)

            if (mappingDir.exists()) {
                def mappings = mappingDir.listFiles()
                mappings.each {
                    buildFeature(it, rocinante, spec)
                }
            }
        }

        spec.features[0].excluded = true
    }

    private buildFeature(File mappingFile, Rocinante rocinante, SpecInfo spec) {
        def clazz = spec.reflection

        def mapping = slurper.parseText(mappingFile.text)

        def condition = rocinante.condition().newInstance(mapping, mapping)

        clazz.getDeclaredMethods().each { Method method ->
            FeatureMetadata metadata = method.getAnnotation(FeatureMetadata.class)
            if (metadata) {
                method.accessible = true
                def feature = createFeature(mappingFile, mapping, method, spec)
                if (!condition()) {
                    log.warning("The feature method '$feature.name' condition not satisfied.")
                    feature.skipped = true
                }
                spec.addFeature(feature)
            }
        }
    }

    private FeatureInfo createFeature(mappingFile, mapping, method, spec) {
        def name = buildName(mappingFile.name)

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

    private String buildName(name) {
        def splitted = name.split('\\?')
        def slug = splitted[0] - '.json'
        def result = slug.replaceAll('/', '-').replaceAll('\\.', '_')
        if (splitted.size() > 1) {
            def rest = splitted[1]
            def params = rest.split('&')
            def keys = params*.split('=')*.first()
            result << [' (', keys.join(','), ','].join()
        }
        result
    }
}
