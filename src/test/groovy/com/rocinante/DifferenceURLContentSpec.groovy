package com.rocinante

import com.rocinante.utils.ParallelIterator
import groovy.json.JsonSlurper
import spock.lang.Specification

import static com.rocinante.utils.TretaAssert.compare

@Rocinante(condition = { response.status == 200 })
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
        def expected = parser.parseText(json)
        def actually = parser.parseText(real)
        def iterator = new ParallelIterator(expected, actually)

        expect:
        iterator.collect {
            def (expect, actual, index) = it;
            compare(expect, actual, index)
        }.every()
    }
}