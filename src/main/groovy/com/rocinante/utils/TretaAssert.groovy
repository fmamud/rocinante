package com.rocinante.utils

class TretaAssert {
    static def compare(expect, actual, index, key = null) {
        if (expect instanceof Map && actual instanceof Map) {
            def values = expect*.key
            values.each { String currentKey ->
                compare(expect[currentKey], actual[currentKey], index, "${key?:''}.$currentKey")
            }
        } else if (expect instanceof List && actual instanceof List) {
            int size  = expect.size() - 1
            0.upto(size) {
                compare(expect[it], actual[it], index, key)
            }
        } else {
            assert expect == actual : "The [$index]$key field does not match.\n\nExpect: $expect\n\nActual: ${actual ?: 'empty or does not exists'}\n\n"
            expect == actual
        }
    }
}
