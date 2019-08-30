package com.dicoding.picodiploma.mysound

import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var btnSound: Button

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false


    private var myListener: View.OnClickListener = View.OnClickListener {
        if (spLoaded) {
            sp.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSound = findViewById(R.id.btn_soundpool)
        btnSound.setOnClickListener(myListener)

        sp = SoundPool.Builder()
                .setMaxStreams(10)
                .build()

        /*
        Tambahkan listener ke soundpool jika proses load sudah selesai
         */
        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                spLoaded = true
            } else {
                Toast.makeText(this@MainActivity, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }

        /*
        Load raw clinking_glasses ke soundpool, jika selesai maka id nya dimasukkan ke variable soundId
         */
        soundId = sp.load(this, R.raw.clinking_glasses, 1) // in 2nd param u have to pass your desire ringtone
    }

}
