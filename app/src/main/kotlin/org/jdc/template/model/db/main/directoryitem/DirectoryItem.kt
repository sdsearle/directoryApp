package org.jdc.template.model.db.main.directoryitem

import androidx.room.DatabaseView

@DatabaseView(
    viewName = DirectoryItem.VIEW_NAME,
    value = DirectoryItem.VIEW_QUERY
)
data class DirectoryItem(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val image: String
) {

    fun getFullName() = "$firstName $lastName"

    companion object {
        const val VIEW_NAME = "directory_item"
        const val VIEW_QUERY = "SELECT id, lastName, firstName, image FROM individual"
    }
}