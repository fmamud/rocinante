package com.rocinante

@Singleton
class Config {

    private static final Properties gradleProperties

    static {
        gradleProperties = new Properties()
        gradleProperties.load(new FileInputStream('gradle.properties'))
    }

    def propertyMissing(String name) {
        System.properties.getProperty(name) ?: gradleProperties.getProperty(name)
    }

    static Set properties() {
        gradleProperties.keySet()
    }
}