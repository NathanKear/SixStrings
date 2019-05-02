package nk.sixstrings.models

sealed class Instrument {
    object Guitar : Instrument()
    object Ukulele : Instrument()
}