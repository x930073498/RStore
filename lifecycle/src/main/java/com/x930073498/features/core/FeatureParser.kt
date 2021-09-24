package com.x930073498.features.core

fun interface FeatureParser<C, F : IFeature> {
    fun parse(data: C): F?
}

@PublishedApi
internal class TypeInstanceFeatureParser<C, F : IFeature>(private val clazz: Class<F>) :
    FeatureParser<C, F> {
    override fun parse(data: C): F? {
        return if (clazz.isInstance(data)) clazz.cast(data) else null
    }
}

@PublishedApi
internal class WrapperFeatureParser<C, F : IFeature>(
    private val targetClass: Class<C>,
    private val feature: F
) : FeatureParser<C, F> {
    override fun parse(data: C): F? {
        return if (targetClass.isInstance(data)) feature else null
    }

}

operator fun <C, F : IFeature> FeatureParser<C, F>.plus(parser: FeatureParser<C, F>): FeatureParser<C, F> {
    return FeatureParser {
        parse(it) ?: parser.parse(it)
    }
}