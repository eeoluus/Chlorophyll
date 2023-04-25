package com.example.chlorophyll.util.extensions.list

val <T> List<T>.first: T
    get() = component1()

val <T> List<T>.second: T
    get() = component2()

val <T> List<T>.third: T
    get() = component3()