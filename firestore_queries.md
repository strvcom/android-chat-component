# Firestore dev docs

### **Code examples of basic queries**

#### To get all conversations for me as a user:

```
fun getFirestoreConversations() = firestoreDb //this is a reference to the firestore database that I'm connected to
        .collection("conversations") //here I want to look through collection of conversations
        .whereArrayContains("members", "83") //pick those that I'm a member of - here you pass your userId you get from our BE as a String
        .orderBy(FieldPath.of("last_message", "timestamp"), Query.Direction.DESCENDING) // you want to order from the newest to the oldest
        .addOnSuccessListener {}
	    .addOnFailureListener {}
```
        
#### To obtain the last 50 messages for a specific conversation, use a code similar to this:

```
fun getFirestoreChatMessages() = firestoreDb //this is a reference to the firestore database that I'm connected to
        .collection("conversations") 
        .document(conversationId) // the conversation whose messages I want
        .collection("messages") //here I want to look through collection of messages
        .limit(50) // limit messages to 50 in order not to load too much data unnecessarily - then we'll implement some paging 
        .orderBy("timestamp", Query.Direction.DESCENDING) // order from the newest
        .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            log("document snapshot fetched: $documentSnapshot") // you get your messages here in the callback, need to parse the documentSnapshot
        }
```

#### To lazy load more messages after you load the 50

This is fairly easy, you just need to keep track of a timestamp of the oldest message that you loaded and then load additional 50 before that

```
fun getFirestoreChatMessages() = firestoreDb //this is a reference to the firestore database that I'm connected to
        .collection("conversations") 
        .document(conversationId) // the conversation whose messages I want
        .collection("messages") //here I want to look through collection of messages
        .orderBy("timestamp", Query.Direction.DESCENDING) // order from the newest
        .startAfter(documentSnapshot)	//This is the documentSnapshot of the oldest message and you want to load older messages from this. 
        .limit(50) // limit messages to 50 in order not to load too much data unnecessarily - then we'll implement some paging 
        .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            log("document snapshot fetched: $documentSnapshot") // you get your messages here in the callback, need to parse the documentSnapshot
        }
```
        
#### Sending a message

The message sending works like this:
 - The conversation between two or more users has to be created beforehand, and the client shouldn't be created by the client (TBD how this will work)
If a user sends a message, the client (Android/iOS) has to complete three writes to Firestore:
 1) creates a message with the respective data
 2) overwrites the last message in the conversation they just added 
 3) set the last seen message of the sending user to the one just sent in the `seen` sub-collection 

These three actions are executed atomically in a transaction (or so called batch write action)
```
val writeBatch = firestoreDb.batch()

val conversationCollection = firestoreDb.collection("conversations")
val conversationDocument = conversationCollection.document(conversationId) // a document of the conversation to update

val messagesCollection = conversationDocument.collection("messages")
val messagesDocument = messagesCollection.document() // creates an empty document to receive message_id to update seen-collection

writeBatch.set(messagesDocument, message)
writeBatch.update(conversationDocument, "last_message", message)
writeBatch.update(conversationDocument, "seen.userId", seenData)
	
//Commit the batch and set listeners for success and failure
writeBatch.commit()						
    .addOnSuccessListener {}
    .addOnFailureListener {}
```

#### Setting a message as read
If a user sees a message, an appropriate document in the `seen` collection needs to be updated.
This is the function I'm using to achieve that
```
fun setMessageAsRead() { 
    firestoreDb  // this is a reference to the firestore database that I'm connected to
        .collection("conversations") // the conversation whose messages I want
        .document(conversationId) // the conversation whose message I want to mark as read
        .update("seen.userId", seenData) // set new data
        .addOnSuccessListener {}
        .addOnFailureListener {}
}
```

#### Loading user data

The data of the users will be downloaded periodically (by default not more often than once per hour) to keep it up to date and saved into a local database with a timestamp of the last update.
The connection to the user's collection will never be opened for longer periods and only for the one-time request.

```
fun getFirestoreUser() = firestoreDb  // this is a reference to the firestore database that I'm connected to
    .collection("users")  // here I want to look through collection of users
    .document(userId)  // the user want
    .addOnSuccessListener {}
    .addOnFailureListener {}
```

### **Code examples of additional queries**

#### Create a conversation:
``` 
val conversationCollection = firestoreDb.collection("conversations")
val conversationDocument = conversationCollection.document() // creates an empty document 

conversationDocument.set(conversationData) // set data to the document
    .addOnSuccessListener {}
	.addOnFailureListener {}
```

#### Update a member:
```
firestoreDb  // this is a reference to the firestore database that I'm connected to
    .collection("users")  // here I want to look through collection of users
    .document(userId)  // the user I want to update
    .update(memberData) // the updated structure of the user
    .addOnSuccessListener {}
    *.addOnFailureListener* {}
```