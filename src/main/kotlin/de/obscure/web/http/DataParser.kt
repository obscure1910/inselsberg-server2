package de.obscure.web.http

import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class DataParser(private val dataRaw: String) {

    val data: Array<String> = splittDoubleCross()


    fun timestampOfData(): Instant {
        val info = data[0].split("//").last()
        val date = findDate(info)
        val time = findTime(info)
        return if (date != null && time != null) {
            parseDateTimeToInstant(time, date)
        } else {
            Instant.from(LocalDateTime.now(ZoneId.of("Europe/Berlin")))
        }
    }

    fun temperature(): Float = findNumber(data[1])?.toFloat() ?: 0.0f
    fun humidity(): Float = findNumber(data[2])?.toFloat() ?: 0.0f
    fun airPressure(): Float = findNumber(data[3])?.toFloat() ?: 0.0f
    fun airPressureTrend(): String = splitDoubleColon(data[4])[1].trim()
    fun windVelocity(): Float = findNumber(data[5])?.toFloat() ?: 0.0f
    fun windGust(): Float = findNumber(data[6])?.toFloat() ?: 0.0f
    fun rain(): Float = findNumber(data[7])?.toFloat() ?: 0.0f
    fun rainLastHours(): Float = findNumber(splitDoubleColon(data[8])[1])?.toFloat() ?: 0.0f
    fun isLiftOpen(): Boolean = data.size > 9 && findSkiliftOpen(data[9])

    private fun findSkiliftOpen(s: String): Boolean = Regex("(.)*(geöffnet){1}(.)*").matches(s)
    private fun findNumber(s: String): String? = Regex("-?\\d+[.,]?\\d+").find(s)?.value?.replace(",", ".")
    private fun findDate(s: String): String? =
        Regex("(31|30|[012]\\d|\\d)\\.(0\\d|1[012]|\\d)\\.(19[789]\\d|20[0124]\\d)").find(s)?.value

    private fun findTime(s: String): String? = Regex("([01]?\\d|2[0-3]):([0-5]?\\d)").find(s)?.value

    private fun parseDateTimeToInstant(time: String, date: String): Instant =
        Instant.from(LocalDateTime.from(fmt.parse("$time $date")).atZone(ZoneId.of("Europe/Berlin")))
            .truncatedTo(ChronoUnit.SECONDS)

    private fun splitDoubleColon(str: String): Array<String> =
        str.split(":").toTypedArray()

    private fun splittDoubleCross(): Array<String> =
        dataRaw.trim().split(Regex("""\+\+""")).toTypedArray()

    companion object {
        private val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
        val LOG = LoggerFactory.getLogger(DataParser::class.java)
    }

}