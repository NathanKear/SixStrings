package nk.sixstrings.models

import java.util.*

data class TabInfo(val songId: String = UUID.randomUUID().toString(), val tab: String = "", val capo: Int = 0, val tuning: String = "Standard (eBGDAE)", val instrument: Instrument = Instrument.Guitar)