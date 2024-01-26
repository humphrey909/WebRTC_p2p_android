package com.codewithkael.webrtcprojectforrecord

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codewithkael.webrtcprojectforrecord.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.enterBtn.setOnClickListener {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
                ).request{ allGranted, _ ,_ ->
                    if (allGranted){
                        startActivity(
                            //숫자만 입력해서 user idx를 넘겨주는 식으로 진행하겠음.
                            Intent(this,CallActivity::class.java)
                                .putExtra("username",binding.username.text.toString())
                        )
                    } else {
                        Toast.makeText(this,"you should accept all permissions",Toast.LENGTH_LONG).show()
                    }
                }

        }

    }
}