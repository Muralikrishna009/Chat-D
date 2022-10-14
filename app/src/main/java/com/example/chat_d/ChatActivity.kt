package com.example.chat_d

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var sendMessage: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var dbRef : DatabaseReference

    var senderRoom: String? = null
    var receiverRoom: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        messageRecyclerView = findViewById(R.id.char_recycler_layout)
        sendMessage = findViewById(R.id.sendMessage)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        dbRef = FirebaseDatabase.getInstance().reference

        //Getting data from database
        dbRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnap in snapshot.children){
                        val messageDb = postSnap.getValue(Message::class.java)
                        messageList.add(messageDb!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        sendButton.setOnClickListener{
            val message = sendMessage.text.toString()
            val messageObject = Message(message, senderUid!!)

            dbRef.child("Chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnCompleteListener{
                    dbRef.child("Chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            sendMessage.setText("")
        }

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

    }
}