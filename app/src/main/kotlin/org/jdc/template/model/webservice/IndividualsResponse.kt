package org.jdc.template.model.webservice

import kotlinx.serialization.Serializable

@Serializable
class IndividualsResponse(val individuals: List<Individual>)