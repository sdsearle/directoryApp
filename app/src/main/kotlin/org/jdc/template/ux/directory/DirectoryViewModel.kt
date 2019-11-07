package org.jdc.template.ux.directory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.vikingsen.inject.viewmodel.ViewModelInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jdc.template.model.db.main.directoryitem.DirectoryItem
import org.jdc.template.model.db.main.individual.Individual
import org.jdc.template.model.db.main.type.IndividualType
import org.jdc.template.model.repository.IndividualRepository
import org.jdc.template.model.webservice.IndividualService
import org.jdc.template.model.webservice.IndividualsResponse
import org.jdc.template.ui.viewmodel.BaseViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import retrofit2.Response
import timber.log.Timber

class DirectoryViewModel
@ViewModelInject constructor(
        private val individualRepository: IndividualRepository,
        private val individualService: IndividualService,
        @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel<DirectoryViewModel.Event>() {

    fun getDirectoryList(): Flow<List<DirectoryItem>> {
        return individualRepository.getDirectoryListFlow()
    }

    fun addIndividual() {
        sendEvent(Event.NewIndividualEvent)
    }

    fun onDirectoryIndividualClicked(directoryListItem: DirectoryItem) {
        sendEvent(Event.ShowIndividualEvent(directoryListItem.id))
    }

    fun callIndividuals()  = viewModelScope.launch {
        val response = individualService.individuals()

        if (response.isSuccessful) {
            processWebServiceResponse(response)
        } else {
            Timber.e("Failed to get colors from webservice [${response.errorBody()}]")
        }
    }

    private suspend fun processWebServiceResponse(response: Response<IndividualsResponse>) {
        if (response.isSuccessful) {
            Timber.i("Search SUCCESS")
            addIndividual(response.body()?.individuals)
        } else {
            Timber.e("Search FAILURE: code (%d)", response.code())
        }
    }

    private suspend fun addIndividual(individuals: List<org.jdc.template.model.webservice.Individual>?) {
        if (individuals != null) {
            for (individual in individuals){
                var newIndividual = Individual(0, 0, IndividualType.HEAD, individual.firstName, individual.lastName,
                        LocalDate.parse(individual.birthdate), LocalTime.now(), OffsetDateTime.now(), "", "", false,
                        individual.profilePicture)
                individualRepository.saveIndividual(newIndividual)
            }
        }
    }

    sealed class Event {
        object NewIndividualEvent : Event()
        data class ShowIndividualEvent(val individualId: Long) : Event()
    }
}