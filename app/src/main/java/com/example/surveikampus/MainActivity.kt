package com.example.surveikampus // SESUAIKAN DENGAN PACKAGEMU

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Menggunakan Companion Object agar objek MediaPlayer bisa diakses bersama antar halaman
    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var isSoundLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inisialisasi Musik Latar (Hanya jika belum pernah dinyalakan)
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }

        // 2. Inisialisasi SoundPool dengan Listener agar tahu kapan audio siap pakai
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        // Ganti baris inisialisasi SoundPool lama dengan 1 baris universal ini:
        soundPool = SoundPool(1, android.media.AudioManager.STREAM_MUSIC, 0)

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) isSoundLoaded = true
        }
        soundId = soundPool.load(this, R.raw.sound_click, 1)

        val btnLihatHasil = findViewById<Button>(R.id.btnLihatHasil)
        btnLihatHasil.setOnClickListener {
            // Mainkan efek suara jika sudah selesai dimuat
            if (isSoundLoaded) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            }

            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // Jika level memori menandakan aplikasi disembunyikan/minimize ke latar belakang
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            MainActivity.mediaPlayer?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        // Saat pengguna balik lagi masuk ke dalam aplikasi, musik otomatis jalan lagi
        if (MainActivity.mediaPlayer?.isPlaying == false) {
            MainActivity.mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            MainActivity.mediaPlayer?.stop()
            MainActivity.mediaPlayer?.release()
            MainActivity.mediaPlayer = null
        }
        soundPool.release()
    }
}