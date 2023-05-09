package org.avr.notes.api.v1

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.avr.notes.api.v1.models.IFolderRequest
import org.avr.notes.api.v1.models.IFolderResponse
import org.avr.notes.api.v1.models.INoteRequest
import org.avr.notes.api.v1.models.INoteResponse

val apiV1Mapper = ObjectMapper().apply {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}

fun apiV1FolderRequestSerialize(request: IFolderRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IFolderRequest> apiV1FolderRequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IFolderRequest::class.java) as T

fun apiV1FolderResponseSerialize(response: IFolderResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IFolderResponse> apiV1FolderResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IFolderResponse::class.java) as T

fun apiV1NoteRequestSerialize(request: INoteRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : INoteRequest> apiV1NoteRequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, INoteRequest::class.java) as T

fun apiV1NoteResponseSerialize(response: INoteResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : INoteResponse> apiV1NoteResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, INoteResponse::class.java) as T