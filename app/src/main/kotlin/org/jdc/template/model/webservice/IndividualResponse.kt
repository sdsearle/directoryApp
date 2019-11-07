package org.jdc.template.model.webservice

import kotlinx.serialization.Serializable

@Serializable
class Individual (val id: Long, val firstName: String, val lastName: String, val birthdate: String, val profilePicture: String, val forceSensitive: Boolean, val affiliation: String)