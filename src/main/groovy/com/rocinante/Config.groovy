package com.rocinante

class   Config {

    Properties gradleProperties

    Config() {
        gradleProperties = new Properties()
        gradleProperties.load(new FileInputStream('gradle.properties'))
    }

    def propertyMissing(String name) {
        System.properties.getProperty(name) ?: gradleProperties.getProperty(name)
    }
}