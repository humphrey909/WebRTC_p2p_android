package com.codewithkael.webrtcprojectforrecord

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codewithkael.webrtcprojectforrecord.databinding.ActivityCallBinding
import com.codewithkael.webrtcprojectforrecord.models.IceCandidateModel
import com.codewithkael.webrtcprojectforrecord.models.MessageModel
import com.codewithkael.webrtcprojectforrecord.utils.NewMessageInterface
import com.codewithkael.webrtcprojectforrecord.utils.PeerConnectionObserver
import com.codewithkael.webrtcprojectforrecord.utils.RTCAudioManager
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription

class CallActivity : AppCompatActivity(), NewMessageInterface {


    lateinit var binding : ActivityCallBinding
    private var userName:String?=null
    private var socketRepository:SocketRepository?=null
    private var rtcClient : RTCClient?=null
    private val TAG = "CallActivity"
    private var target:String = "" //??
    private val gson = Gson()
    private var isMute = false
    private var isCameraPause = false
    private val rtcAudioManager by lazy { RTCAudioManager.create(this) }
    private var isSpeakerMode = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        createSocket() //소켓 리스너 부분

    }

    private fun init(){
        userName = intent.getStringExtra("username")
        socketRepository = SocketRepository(this)
        userName?.let { socketRepository?.initSocket(it) } //소켓 연결
        rtcClient = RTCClient(application,userName!!,socketRepository!!, object : PeerConnectionObserver() {
            override fun onIceCandidate(p0: IceCandidate?) {
                super.onIceCandidate(p0)
                rtcClient?.addIceCandidate(p0)
                val candidate = hashMapOf(
                    "sdpMid" to p0?.sdpMid,
                    "sdpMLineIndex" to p0?.sdpMLineIndex,
                    "sdpCandidate" to p0?.sdp
                )

                //rtcClient에 추적이 잡히는 ice가 있다면 전송
                socketRepository?.sendMessageToSocket(
                    "ice_candidate",
                    MessageModel(userName,target,candidate)
                )

            }

            override fun onAddStream(p0: MediaStream?) {
                super.onAddStream(p0)
                p0?.videoTracks?.get(0)?.addSink(binding.remoteView)
                Log.d(TAG, "onAddStream: $p0")

            }
        })
        rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)


        //버튼 기능 바인딩하는 부분
        binding.apply {

            //who to call? 에서 전화 걸기 버튼
            callBtn.setOnClickListener {
                socketRepository?.sendMessageToSocket(
                    "start_call",
                    MessageModel(
                    userName,targetUserNameEt.text.toString(),null
                ))
                target = targetUserNameEt.text.toString()
            }

            switchCameraButton.setOnClickListener {
                rtcClient?.switchCamera()
            }

            micButton.setOnClickListener {
                if (isMute){
                    isMute = false
                    micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
                }else{
                    isMute = true
                    micButton.setImageResource(R.drawable.ic_baseline_mic_24)
                }
                rtcClient?.toggleAudio(isMute)
            }

            videoButton.setOnClickListener {
                if (isCameraPause){
                    isCameraPause = false
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
                }else{
                    isCameraPause = true
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
                }
                rtcClient?.toggleCamera(isCameraPause)
            }

            audioOutputButton.setOnClickListener {
                if (isSpeakerMode){
                    isSpeakerMode = false
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_hearing_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.EARPIECE)
                }else{
                    isSpeakerMode = true
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

                }

            }
            endCallButton.setOnClickListener {
                setCallLayoutGone()
                setWhoToCallLayoutVisible()
                setIncomingCallLayoutGone()
                rtcClient?.endCall()
            }
        }
    }


    private fun createSocket() {
//        socketRepository?.Socket_Listener()
//        socketRepository?.mSocket?.on(Socket.EVENT_CONNECT, onConnect)

//        mSocket.on(Socket.EVENT_CONNECT, onConnect)
//        mSocket.on("all_users", all_users)
//        mSocket.on("getOffer", getOffer)
//        mSocket.on("getAnswer", getAnswer)
//        mSocket.on("getCandidate", getCandidate)
    }





    //websocket 메시지 오면 받는 부분
    override fun onNewMessage(commend:String,message: MessageModel) {
        Log.d(TAG, "onNewMessage: $message")
//        when(message.commend){
//
//            //전화 왔다고 알려주는 부분
//            "call_response"->{
//                if (message.data == "user is not online"){ //유저가 없는 경우
//                    //user is not reachable
//                    runOnUiThread {
//                        Toast.makeText(this,"user is not reachable",Toast.LENGTH_LONG).show()
//
//                    }
//                }else{ //전화 알람이 간 경우
//                    //we are ready for call, we started a call
//                    runOnUiThread {
//                        setWhoToCallLayoutGone() //기존화면 off
//                        setCallLayoutVisible() //전화 화면 on
//
//                        //기능 연결
//                        binding.apply {
//                            rtcClient?.initializeSurfaceView(localView)
//                            rtcClient?.initializeSurfaceView(remoteView)
//                            rtcClient?.startLocalVideo(localView)
//
//                            //sdp offer 시작하는 부분 - 전화를 받은 사람이 먼저 offer 보냄
//                            rtcClient?.call(targetUserNameEt.text.toString())
//                        }
//                    }
//                }
//            }
//
//            //전화 건사람이 answer 받음
//            "answer_received" ->{
//
//                val session = SessionDescription(
//                    SessionDescription.Type.ANSWER,
//                    message.data.toString()
//                )
//                rtcClient?.onRemoteSessionReceived(session)
//                runOnUiThread {
//                    binding.remoteViewLoading.visibility = View.GONE
//                }
//            }
//
//            //전화 건 사람이 offer 받음
//            "offer_received" ->{
//                runOnUiThread {
//                    setIncomingCallLayoutVisible()
//                    binding.incomingNameTV.text = "${message.userid.toString()} is calling you"
//                    binding.acceptButton.setOnClickListener {
//                        setIncomingCallLayoutGone()
//                        setCallLayoutVisible()
//                        setWhoToCallLayoutGone()
//
//                        binding.apply {
//                            rtcClient?.initializeSurfaceView(localView)
//                            rtcClient?.initializeSurfaceView(remoteView)
//                            rtcClient?.startLocalVideo(localView)
//                        }
//                        val session = SessionDescription(
//                            SessionDescription.Type.OFFER,
//                            message.data.toString()
//                        )
//                        rtcClient?.onRemoteSessionReceived(session)
//
//                        //전화 받은 사람에게 answer 전송
//                        rtcClient?.answer(message.userid!!)
//                        target = message.userid!!
//                        binding.remoteViewLoading.visibility = View.GONE
//
//                    }
//                    binding.rejectButton.setOnClickListener {
//                        setIncomingCallLayoutGone()
//                    }
//
//                }
//
//            }
//
//            //전송된 ice_candidate를 상대방에게 전송하여 서로 주고 받음
//            "ice_candidate"->{
//                try {
//                    val receivingCandidate = gson.fromJson(gson.toJson(message.data),
//                        IceCandidateModel::class.java)
//
//                    //ice_candidate 저장
//                    rtcClient?.addIceCandidate(IceCandidate(receivingCandidate.sdpMid,
//                        Math.toIntExact(receivingCandidate.sdpMLineIndex.toLong()),receivingCandidate.sdpCandidate))
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//            }
//        }
    }

    private fun setIncomingCallLayoutGone(){
        binding.incomingCallLayout.visibility = View.GONE
    }
    private fun setIncomingCallLayoutVisible() {
        binding.incomingCallLayout.visibility = View.VISIBLE
    }

    private fun setCallLayoutGone() {
        binding.callLayout.visibility = View.GONE
    }

    private fun setCallLayoutVisible() {
        binding.callLayout.visibility = View.VISIBLE
    }

    private fun setWhoToCallLayoutGone() {
        binding.whoToCallLayout.visibility = View.GONE
    }

    private fun setWhoToCallLayoutVisible() {
        binding.whoToCallLayout.visibility = View.VISIBLE
    }
}