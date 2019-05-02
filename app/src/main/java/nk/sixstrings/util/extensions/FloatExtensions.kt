package nk.sixstrings.util.extensions

fun Float.clamp(min: Float, max: Float): Float {
    return Math.max(Math.min(max, this), min)
}