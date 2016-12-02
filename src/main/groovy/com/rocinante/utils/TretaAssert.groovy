package com.rocinante.utils

class TretaAssert {
    static def compare(expect, actual, int index, String key = null) {
        if (expect instanceof Map && actual instanceof Map) {
            def values = expect*.key
            values.eachWithIndex { String currentKey, Integer currentIndex ->
                compare(expect[currentKey], actual[currentKey], currentIndex, "${key ?: ''}.$currentKey${expect[currentKey] instanceof List ? '' : "[$currentIndex]"}")
            }
        } else if (expect instanceof List && actual instanceof List) {
            int size = expect.size() - 1
            0.upto(size) {
                compare(expect[it], actual[it], index, key)
            }
        } else {
            def that = expect ?: actual
            if (that?.metaClass?.respondsTo(that, 'getValue')) {
                def (thatExpect, thatActual) = [expect, actual]*.value.collect { it instanceof List ? it?.first() : it }
                compare(thatExpect, thatActual, Math.max(index - 1, 0), "${key ?: ''}.${expect.key}")
            } else {
                assert expect == actual: "The $key field does not match.\n\nExpect: $expect\n\nActual: ${actual ?: 'empty or does not exists'}\n\n"
                expect == actual
            }
        }
    }
}