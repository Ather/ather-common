package com.atherapp.common.json.gson

import com.atherapp.common.extensions.lowerCase
import com.atherapp.common.extensions.to24HourString
import com.atherapp.common.extensions.toZoneString
import com.github.salomonbrys.kotson.toJson
import com.google.gson.*
import java.lang.reflect.Type
import java.time.*
import java.time.format.DateTimeFormatter

object DayOfWeekTypeAdapter : JsonDeserializer<DayOfWeek>, JsonSerializer<DayOfWeek> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DayOfWeek? = if (json != null) DayOfWeek.valueOf(json.asString.toUpperCase()) else null

    override fun serialize(src: DayOfWeek?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? = src?.lowerCase()?.toJson()
}

object LocalDateTypeAdapter : JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate? = if (json != null) LocalDate.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE) else null

    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? = src?.toString()?.toJson()
}

object LocalTimeTypeAdapter : JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime? = if (json != null) LocalTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_TIME) else null

    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? = src?.to24HourString()?.toJson()
}

/**TODO
class ZonedDateTimeTypeAdapter(
        private val localTimezone: ZoneId = ZoneId.systemDefault()
) : JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ZonedDateTime? = if (json != null) Instant.parse(json.asString).atZone(TraktConfiguration.traktTimezone).fromTraktZone(localTimezone) else null

    override fun serialize(src: ZonedDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? = DateTimeFormatter.ISO_INSTANT.format(src?.toTraktZone()).toJson()
}*/

object ZoneIdTypeAdapter : JsonDeserializer<ZoneId>, JsonSerializer<ZoneId> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ZoneId? = if (json != null) ZoneId.of(json.asString) else null

    override fun serialize(src: ZoneId?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? = src?.toZoneString()?.toJson()
}