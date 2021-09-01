package com.x930073498.rstore.core

sealed class Feature(private val flag: Int) {

    companion object : Feature(0) {
        operator fun Feature.plus(feature: Feature): Feature {
            return WrapFeature(flag or feature.flag)
        }


        operator fun Feature.minus(feature: Feature): Feature {
            return WrapFeature(flag and feature.flag.inv())
        }

        operator fun Feature.contains(feature: Feature): Boolean {
            return hasFeature(feature)
        }

        fun Feature.replace(feature: Feature): Feature {
            return WrapFeature(feature.flag.inv() or feature.flag and flag or feature.flag)
        }

        internal fun Feature.hasFeature(feature: Feature): Boolean {
            return (feature.flag and flag) != 0
        }
    }

    object SaveState : Feature(1)
    object Anchor : Feature(1 shl 1)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Feature

        if (flag != other.flag) return false

        return true
    }

    override fun hashCode(): Int {
        return flag
    }

    override fun toString(): String {
        return "Feature(flag=$flag,className=${javaClass})"
    }


}


internal object PropertyChanged : Feature(1 shl 2)

internal class WrapFeature(flag: Int) : Feature(flag)

internal object AnchorUnregister : Feature(1 shl 3)


