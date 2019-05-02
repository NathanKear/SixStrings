package nk.sixstrings.models

data class TabInfo(val tab: String, val capo: Int = 0, val tuning: String = "Standard (eBGDAE)", val instrument: Instrument = Instrument.Guitar)