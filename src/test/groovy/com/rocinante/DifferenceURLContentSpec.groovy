package com.rocinante

import groovy.json.JsonSlurper
import org.spockframework.lang.ConditionBlock
import spock.lang.Specification

@Rocinante
class DifferenceURLContentSpec extends Specification {

    @Rocinante(config = 'host')
    String basePath

    @Rocinante(binding = 'request.url')
    String url

    @Rocinante(binding = 'response.bodyFileName', isFile = true)
    String json

    def parser = new JsonSlurper()

    def "should make diff of my tapes"() {
        given:
        def real = [basePath, url].join().toURL().text
        def that = parser.parseText(json)
        def other = parser.parseText(real)

        def sample = that[0]*.key

        expect:
        (0..<that.size()).each { idx ->
            sample.each { def field ->
                assert that[idx][field] == other[idx][field]: "The $field field does not match.(that: ${that[idx][field]}, other: ${other[idx][field]})"
            }
        }
    }
}
