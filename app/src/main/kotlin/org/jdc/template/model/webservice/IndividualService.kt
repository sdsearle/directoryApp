package org.jdc.template.model.webservice

import org.jdc.template.model.webservice.colors.dto.ColorsDto
import retrofit2.Response
import retrofit2.http.GET

interface IndividualService {
    @GET("/mobile/interview/directory")
    suspend fun individuals(): Response<IndividualsResponse>

    companion object {
        const val BASE_URL = "https://ldscdn.org"
    }


}