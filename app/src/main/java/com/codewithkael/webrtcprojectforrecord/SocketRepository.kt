package com.codewithkael.webrtcprojectforrecord

import android.util.Log
import com.codewithkael.webrtcprojectforrecord.models.MessageModel
import com.codewithkael.webrtcprojectforrecord.utils.NewMessageInterface
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException


class SocketRepository (private val messageInterface: NewMessageInterface) {
//    private var webSocket: WebSocketClient? = null
    private var userName: String? = null
    private val TAG = "SocketRepository"
    private val gson = Gson()
    private var mSocket: Socket? = null

    fun initSocket(username: String) {
        userName = username
        //if you are using android emulator your local websocket address is going to be "ws://10.0.2.2:3000"
        //if you are using your phone as emulator your local address, use cmd and then write ipconfig
        // and get your ethernet ipv4 , mine is : "ws://192.168.1.3:3000"
        //but if your websocket is deployed you add your websocket address here

//        webSocket = object : WebSocketClient(URI("ws://13.209.75.236:3000")) {
//            //        webSocket = object : WebSocketClient(URI("ws://192.168.1.3:3000")) {
//            override fun onOpen(handshakedata: ServerHandshake?) {
//                sendMessageToSocket(
//                    MessageModel(
//                        "start",username,null,null
//                    )
//                )
//            }
//
//            override fun onMessage(message: String?) {
//                try {
//                    messageInterface.onNewMessage(gson.fromJson(message,MessageModel::class.java))
//
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//
//            }
//
//            override fun onClose(code: Int, reason: String?, remote: Boolean) {
//                Log.d(TAG, "onClose: $reason")
//            }
//
//            override fun onError(ex: Exception?) {
//                Log.d(TAG, "onError: $ex")
//            }
//
//        }
//        webSocket?.connect()


        //IO.socket 메소드는 은 저 URL 을 토대로 클라이언트 객체를 Return 합니다.
        try {
            mSocket = IO.socket("http://13.209.75.236:3000")
            mSocket?.connect()


            Socket_Listener()

            sendMessageToSocket(
                "start",
                MessageModel(
                    username,null,null
                )
            )
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun sendMessageToSocket(commend: String, message: MessageModel) {
        try {
            Log.d(TAG, "sendMessageToSocket: $message")
            mSocket?.emit(commend,Gson().toJson(message))
        } catch (e: Exception) {
            Log.d(TAG, "sendMessageToSocket: $e")
        }
    }

    //소켓 리스너 생성
    fun Socket_Listener(){
        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
//        mSocket?.on("test", test)

        mSocket?.on("enter", enter)
        mSocket?.on("create", create)
        mSocket?.on("delete_user", delete_user)




//        mSocket?.on("all_users", all_users)
        mSocket?.on("offer_received", offer_received)
        mSocket?.on("answer_received", answer_received)
        mSocket?.on("ice_candidate", ice_candidate)
    }
    private val onConnect = Emitter.Listener { // 여기서 다시 "login" 이벤트를 서버쪽으로 username과 함께 보냅니다.
        // 서버 측에서는 이 username을 whoIsON Array에 추가를 할 것입니다.
        //            mSocket.emit("login", username);
        Log.d("Tag", "Socket is connected with ")
    }


//    private val test = Emitter.Listener { args ->
//        val message = args[0] as String
//
//        Log.d("test", "getCandidate")
////        Log.d("test", Arrays.toString(args)) // [org.webrtc.SessionDescription@15f3281]
//        Log.d("test", message) // org.webrtc.SessionDescription@15f3281
//        Log.d("test", "getCandidate")
//
//        var commend = "test"
//        try {
//            messageInterface.onNewMessage(commend, gson.fromJson(args[0].toString(),MessageModel::class.java))
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//    }
    private val enter = Emitter.Listener { args ->
        val message = args[0] as String

        Log.d("enter", "enter")
        Log.d("enter", message) // org.webrtc.SessionDescription@15f3281
        Log.d("enter", "enter")

        var commend = "enter"
        try {
            messageInterface.onNewMessage(commend, gson.fromJson(args[0].toString(),MessageModel::class.java))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private val create = Emitter.Listener { args ->
        val message = args[0] as String

        Log.d("create", "create")
        Log.d("create", message) // org.webrtc.SessionDescription@15f3281
        Log.d("create", "create")

        var commend = "create"
        try {
            messageInterface.onNewMessage(commend, gson.fromJson(args[0].toString(),MessageModel::class.java))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private val delete_user = Emitter.Listener { args ->
        val message = args[0] as String

        Log.d("delete_user", "delete_user")
        Log.d("delete_user", message) // org.webrtc.SessionDescription@15f3281
        Log.d("delete_user", "delete_user")

        var commend = "delete_user"
        try {
            messageInterface.onNewMessage(commend, gson.fromJson(args[0].toString(),MessageModel::class.java))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private val offer_received = Emitter.Listener { args ->
        val message = args[0] as String

        Log.d("offer_received", "offer_received")
        Log.d("offer_received", message) // org.webrtc.SessionDescription@15f3281
        Log.d("offer_received", "offer_received")

        var commend = "offer_received"
        try {
            messageInterface.onNewMessage(commend, gson.fromJson(args[0].toString(),MessageModel::class.java))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private val answer_received = Emitter.Listener { args ->
        val message = args[0] as String

        Log.d("answer_received", "answer_received")
        Log.d("answer_received", message) // org.webrtc.SessionDescription@15f3281
        Log.d("answer_received", "answer_received")

        var commend = "answer_received"
        try {
            messageInterface.onNewMessage(commend, gson.fromJson(args[0].toString(),MessageModel::class.java))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private val ice_candidate = Emitter.Listener { args ->
        val message = args[0] as String

        Log.d("ice_candidate", "ice_candidate")
        Log.d("ice_candidate", message) // org.webrtc.SessionDescription@15f3281
        Log.d("ice_candidate", "ice_candidate")

        var commend = "ice_candidate"
        try {
            messageInterface.onNewMessage(commend, gson.fromJson(args[0].toString(),MessageModel::class.java))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun Socket_Close(){
        mSocket?.disconnect()
    }
}