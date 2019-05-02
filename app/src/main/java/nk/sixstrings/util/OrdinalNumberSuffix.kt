package nk.sixstrings.util

object OrdinalNumberSuffix {

    fun appendOrdinalSuffix(n: Int): String {
        return n.toString() + getOrdinalSuffix(n)
    }

    fun getOrdinalSuffix(n: Int): String {
        if (n >= 11 && n <= 13) {
            return "th"
        }
        return when (n % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}