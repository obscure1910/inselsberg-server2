package de.obscure.web.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class StatisticInformation(var statistics: List<Statistic>)
