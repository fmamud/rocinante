package com.rocinante.utils

class ParallelIterator implements Iterator<Tuple> {

    List iterators = []

    ParallelIterator(...collections) {
        this.iterators = [*collections]*.iterator()
    }

    @Override
    boolean hasNext() {
        iterators*.hasNext().every()
    }

    @Override
    Tuple next() {
        def values = iterators*.next()
        new Tuple(*values)
    }
}