package de.obscure.web.http

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class DataParserTest() : StringSpec({

    val data = """
        |Skilift geschlossen
        |// Aussichtsturm "Großer Inselsberg" täglich von 10-16Uhr geöffnet - Heiraten in 930m Höhe, Thüringens höchstes Trauzimmer - auch als Seminarraum bis 40 Personen nutzbar!
        |// aktuelles Wetter - "Großer Inselsberg" 25.12.2015, 21:50 Uhr
        |++ Temperatur: 6.4 °C
        |++ Luftfeuchte: 100 %
        |++ Luftdruck: 1027,4 hPa
        |++ Luftdrucktendenz: gleichbleibend
        |++ Windgeschwindigkeit: 17.2 km/h
        |++ Windböen: 29.0 km/h
        |++ Niederschlag der letzten Std.: 0.0 l/m²
        |++ Niederschlag der letzten 24 Std.: 0.6 l/m²
        |++ Skilift geschlossen""".trimMargin()

    val data2 = """
        |Skilift geschlossen
        |// Aussichtsturm "Großer Inselsberg" täglich von 10-16Uhr geöffnet - Heiraten in 930m Höhe, Thüringens höchstes Trauzimmer - auch als Seminarraum bis 40 Personen nutzbar!
        |// aktuelles Wetter - "Großer Inselsberg" 25.12.2015, 21:50 Uhr
        |++ Temperatur: 6.4 °C
        |++ Luftfeuchte: 100 %
        |++ Luftdruck: 1027.4 hPa
        |++ Luftdrucktendenz: gleichbleibend
        |++ Windgeschwindigkeit: 17.2 km/h
        |++ max. Wind: 29.0 km/h
        |++ Niederschlag der letzten Std.: 0.0 l/m²
        |++ Niederschlag der letzten 24 Std.: 0.6 l/m²""".trimMargin()

    val data3 = """
        |Skilift geöffnet
        |// Aussichtsturm "Großer Inselsberg" täglich von 10-16Uhr geöffnet - Heiraten in 930m Höhe, Thüringens höchstes Trauzimmer - auch als Seminarraum bis 40 Personen nutzbar!
        |// aktuelles Wetter - "Großer Inselsberg" 25.12.2015, 21:50 Uhr
        |++ Temperatur: 6.4 °C
        |++ Luftfeuchte: 100 %
        |++ Luftdruck: 1027.4 hPa
        |++ Luftdrucktendenz: gleichbleibend
        |++ Windgeschwindigkeit: 17.2 km/h
        |++ max. Wind: 29.0 km/h
        |++ Niederschlag der letzten Std.: 0.0 l/m²
        |++ Niederschlag der letzten 24 Std.: 0.6 l/m²
        |++ Skilift geöffnet""".trimMargin()

    val data4 = """
        |Erlebnis-Aussichtsturm "Großer Inselsberg" täglich von 10-16Uhr geöffnet - freier Eintritt mit der Kurkarte 
        |// Do 12.8. Sternschnuppennacht am Erlebnis-Aussichtsturm von 21:00-23:30Uhr 
        |// aktuelles Wetter - "Großer Inselsberg" 26.07.2021, 20:50 Uhr 
        |++ Temperatur: 16.1 °C 
        |++ Luftfeuchte: 88 % 
        |++ Luftdruck: 1011.1 hPa 
        |++ Luftdrucktendenz: gleichbleibend 
        |++ Windgeschwindigkeit: 1.3 km/h 
        |++ max. Wind: 8.0 km/h 
        |++ Niederschlag der letzten Std.: 0.0 l/m² 
        |++ Niederschlag der letzten 24 Std.: 12.8 l/m² 
        |++ Skilift geschlossen""".trimMargin()

    "Dataparser should be able to parse received text from the remote server" {
        val p = DataParser(data)
        p.data.shouldHaveSize(10)
        p.timestampOfData().toString().shouldBe("2015-12-25T20:50:00Z")
        p.temperature().shouldBe(6.4f)
        p.humidity().shouldBe(100f)
        p.airPressure().shouldBe(1027.4f)
        p.airPressureTrend().shouldBe("gleichbleibend")
        p.windVelocity().shouldBe(17.2f)
        p.windGust().shouldBe(29.0f)
        p.rain().shouldBe(0.0)
        p.rainLastHours().shouldBe(0.6f)
        p.isLiftOpen().shouldBeFalse()
    }

    "Dataparser should be able to recognize that the ski-lift is closed" {
        val p = DataParser(data2)
        p.data.shouldHaveSize(9)
        p.timestampOfData().toString().shouldBe("2015-12-25T20:50:00Z")
        p.temperature().shouldBe(6.4f)
        p.humidity().shouldBe(100f)
        p.airPressure().shouldBe(1027.4f)
        p.airPressureTrend().shouldBe("gleichbleibend")
        p.windVelocity().shouldBe(17.2f)
        p.windGust().shouldBe(29.0f)
        p.rain().shouldBe(0.0)
        p.rainLastHours().shouldBe(0.6f)
        p.isLiftOpen().shouldBeFalse()
    }

    "Dataparser should be able to recognize that the ski-lift is open" {
        val p = DataParser(data3)
        p.data.shouldHaveSize(10)
        p.timestampOfData().toString().shouldBe("2015-12-25T20:50:00Z")
        p.temperature().shouldBe(6.4f)
        p.humidity().shouldBe(100f)
        p.airPressure().shouldBe(1027.4f)
        p.airPressureTrend().shouldBe("gleichbleibend")
        p.windVelocity().shouldBe(17.2f)
        p.windGust().shouldBe(29.0f)
        p.rain().shouldBe(0.0)
        p.rainLastHours().shouldBe(0.6f)
        p.isLiftOpen().shouldBeTrue()
    }

    "Dataparser should be able to ignore announcements of special events" {
        val p = DataParser(data4)
        p.timestampOfData().toString().shouldBe("2021-07-26T18:50:00Z")

    }

})