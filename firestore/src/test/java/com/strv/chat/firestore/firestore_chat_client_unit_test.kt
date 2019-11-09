package com.strv.chat.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.core.data.source.ListSource
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.task.task
import com.strv.chat.firestore.client.FirestoreChatClient
import com.strv.chat.firestore.entity.FirestoreDataEntity
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.model.FirestoreTextMessageModel
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.random.Random

class FirestoreChatClientUnitTest {

    private val conversationId = "conversationId"

    private val messageEntityList = listOf(
        FirestoreMessageEntity(
            "BBsNsWNLfNFAEe9kKjpJ",
            "user-1",
            "text",
            FirestoreDataEntity("Hello the first message"),
            Timestamp(1572873536, 583000000)
        )
    )

    private val messageModelList = listOf(
        FirestoreTextMessageModel(
            "BBsNsWNLfNFAEe9kKjpJ",
            Date(1572873536583),
            "user-1",
            "Hello the first message"
        )
    )

    private val firestoreDb = mockk<FirebaseFirestore>()

    private val collectionReference = mockk<CollectionReference>()

    private val documentReference = mockk<DocumentReference>()

    private val query = mockk<Query>()

    private val listSource = mockk<ListSource<FirestoreMessageEntity>>()

    @Before
    fun setup() {
        every { firestoreDb.collection(any()) } returns collectionReference
        every { collectionReference.document(any()) } returns documentReference
        every { documentReference.collection(any()) } returns collectionReference
    }

    @Test
    fun `successful fetch of messages`() {
        val chatClient = spyk(FirestoreChatClient(firestoreDb))

        val result = spyk(
            Task.TaskImpl<List<FirestoreMessageEntity>, Throwable>().apply {
                invokeSuccess(messageEntityList)
            }
        )

        every { collectionReference.orderBy(any<String>(), any()) } returns query
        every { query.limit(any()) } returns query
        every { query.startAfter(any<Timestamp>()) } returns query
        every { chatClient.firestoreListSource(any()) } returns listSource
        every { listSource.get() } returns result

        chatClient.messages(conversationId, Date(), Random.nextLong())
            .onSuccess { modelList ->
                assertEquals(messageModelList, modelList)
            }
            .onError {
                fail("onError was called with a successful response")
            }

        verify { result.onSuccess(captureLambda()) }
        verify { result.onError(captureLambda()) wasNot Called }
    }

    @Test
    fun `failed fetch of messages`() {
        val throwable = Throwable("Test error")

        val chatClient = spyk(FirestoreChatClient(firestoreDb))

        val result = spyk(
            task<List<FirestoreMessageEntity>, Throwable> {
                invokeError(throwable)
            }
        )

        every { collectionReference.orderBy(any<String>(), any()) } returns query
        every { query.limit(any()) } returns query
        every { query.startAfter(any<Timestamp>()) } returns query
        every { chatClient.firestoreListSource(any()) } returns listSource
        every { listSource.get() } returns result

        chatClient.messages("", Date(), 50)
            .onSuccess { modelList ->
                fail("onSuccess was called with an error response")
            }
            .onError { error ->
                assertEquals(throwable, error)
            }

        verify { result.onSuccess(captureLambda()) wasNot Called }
        verify { result.onError(captureLambda()) }
    }

    @Test
    fun `successful subscription to the messages source`() {
        val chatClient = spyk(FirestoreChatClient(firestoreDb))

        val result = spyk(
            ObservableTask.ObservableTaskImpl<List<FirestoreMessageEntity>, Throwable>({}).apply {
                invokeNext(messageEntityList)
            }
        )

        every { collectionReference.limit(any()) } returns query
        every { query.orderBy(any<String>(), any()) } returns query
        every { query.startAfter(any<Timestamp>()) } returns query
        every { chatClient.firestoreListSource(any()) } returns listSource
        every { listSource.subscribe() } returns result

        chatClient.subscribeMessages(conversationId, Random.nextLong())
            .onNext { modelList ->
                assertEquals(messageModelList, modelList)
            }
            .onError {
                fail("onError was called with a successful response")
            }

        verify(atLeast = 1) { result.onNext(captureLambda()) }
        verify { result.onError(captureLambda()) wasNot Called }

        for (i in 0..4) {
            result.invokeNext(messageEntityList)

            verify(atLeast = 1) { result.onNext(captureLambda()) }
            verify { result.onError(captureLambda()) wasNot Called }
        }
    }

    @Test
    fun `failed subscription to the messages source`() {
        val throwable = Throwable("Test error")

        val chatClient = spyk(FirestoreChatClient(firestoreDb))

        val result = spyk(
            ObservableTask.ObservableTaskImpl<List<FirestoreMessageEntity>, Throwable>({}).apply {
                invokeError(throwable)
            }
        )

        every { collectionReference.limit(any()) } returns query
        every { query.orderBy(any<String>(), any()) } returns query
        every { query.startAfter(any<Timestamp>()) } returns query
        every { chatClient.firestoreListSource(any()) } returns listSource
        every { listSource.subscribe() } returns result

        chatClient.subscribeMessages(conversationId, Random.nextLong())
            .onNext {
                fail("onNext was called with an error response")
            }
            .onError { error ->
                assertEquals(throwable, error)
            }

        verify { result.onNext(captureLambda()) wasNot Called }
        verify { result.onError(captureLambda()) }
    }
}
