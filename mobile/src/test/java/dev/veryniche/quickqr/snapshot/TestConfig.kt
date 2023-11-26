package dev.veryniche.quickqr.snapshot

import com.android.resources.NightMode


data class TestConfig(
    val device: Device,
    val nightMode: NightMode,
    val fontScale: Float,
) {
    override fun toString(): String {
        return "device=$device, nightMode=$nightMode, fontScale=$fontScale"
    }
}

enum class Device {
    PIXEL_6, // phone
//    PIXEL_C, // tablet
}