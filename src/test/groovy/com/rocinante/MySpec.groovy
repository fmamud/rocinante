package com.rocinante

import groovy.json.JsonSlurper
import spock.lang.Specification

class MySpec extends Specification {

    @Rocinante(directory = 'src/test/resources')
    def json

    def parser = new JsonSlurper()

    def "should make diff of my tapes"() {
        given:
        def real = 'http://www.mocky.io/v2/5835a32e1100001c070bffa9'.toURL().text
        def that = parser.parseText(json)
        def other = parser.parseText(real)

        expect:
        that == other
    }
}