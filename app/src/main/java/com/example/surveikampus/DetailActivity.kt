package com.example.surveikampus

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var isSoundLoaded = false
    private var currentIndex = 0

    // 1. STRUKTUR DATA
    data class LaporanFakultas(
        val namaFakultas: String,
        val fotoResId: Int,
        val fotoInterviewResId: Int,
        val skorKelas: Float,
        val detailKelas: String,
        val labelFasilitas: String,
        val skorFasilitas: Float,
        val detailFasilitas: String
    )

    // 2. ARRAY DATA DENGAN FOTO INTERVIEW MASING-MASING FAKULTAS
    private val dataLaporan = arrayOf(
        LaporanFakultas(
            "FAKULTAS FISIP", R.drawable.kelas_fisip, R.drawable.interview_fisip,
            4f, "Narasumber: Muhammad Fari Hanif Abyan\nUntuk kapasitas 144 orang ruangannya tidak memadai. Pada semester 1-4 saat masih memakai gedung fisip lama keluhannya panas. Fasilitas sudah mulai berkembang sejak semester 4, terdapat TV & AC yang dingin.",
            "Kenyamanan Fasilitas: GOR", 3f, "GOR berada dibawah UKM BPOK sehingga mahasiswa umum jarang menggunakan. Beberapa WC rusak."
        ),
        LaporanFakultas(
            "FAKULTAS TI (FTI UNISKA)", R.drawable.kelas_ti, R.drawable.interview_ti,
            4f, "Narasumber: Tri Sandi Febinsa\nCukup luas untuk kapasitas 25 orang mahasiswa. Ruangan sangat dingin dan terdapat LED TV yang berfungsi dengan baik. Ada beberapa lampu yang rusak sehingga kelas menjadi agak gelap. Papan tulis tidak terlalu bagus.",
            "Kenyamanan Fasilitas: Kantin", 3f, "Tempatnya terasa kecil (mungkin karena saat malam hanya satu tempat yang buka). Tempat terasa agak panas walaupun ada beberapa kipas angin. Makanan dan minumannya lumayan enak."
        ),
        LaporanFakultas(
            "FAKULTAS FKIP", R.drawable.kelas_fkip, R.drawable.interview_fkip,
            2f, "Narasumber: Rifki Hayat Kurniawan\nVentilasi di dalam kelas membuat AC tidak terasa dingin. Sebagian kursi tidak layak pakai. Sebagian AC dan kipas angin sudah rusak. Wifi tidak bisa digunakan.",
            "Kenyamanan Fasilitas: Lahan Parkir", 2f, "Kendaraan terparkir dalam area yang teratur sehingga memudahkan pengawasan. Lokasi parkir berada di lingkungan kampus yang umumnya memiliki aktivitas dan lalu-lalang mahasiswa. Sebagian besar area parkir tidak memiliki atap atau kanopi, sehingga kendaraan terpapar panas dan hujan. Pada siang hari, area terbuka dapat terasa cukup panas bagi pengguna."
        ),
        LaporanFakultas(
            "FAKULTAS HUKUM", R.drawable.kelas_hukum, R.drawable.interview_hukum,
            3f, "Narasumber: Muhammad Helman\nUntuk ruang kelas yang biasanya digunakan 60-70 orang mahasiswa terkadang ruangan tersebut bisa memadai dan terkadang kurang karena sarana kelas yang tidak merata. AC yang bisa digunakan hanya pada beberapa kelas. Proyektor materi hanya beberapa saja yang bisa digunakan. Jarak kursi sempit. Ruang kelas panas karena minim ventilasi udara.",
            "Kenyamanan Fasilitas: Kantin", 4f, "Semester 1-2 tidak ada kantin. Semester 3 sudah dibangun, untuk kualitasnya biasa saja tapi cukup. Jika beberapa jadwal kelas selesai bersamaan jadi sempit dan berdesakan. Tempat untuk makan cukup; tidak bagus juga tidak buruk."
        ),
        LaporanFakultas(
            "FAKULTAS FEB", R.drawable.kelas_feb, R.drawable.interview_feb,
            5f, "Narasumber: Muhammad Rizky\nSecara keseluruhan ruangan kelas sangat nyaman dan bagus. Ada beberapa kelas yang fasilitasnya kurang, seperti AC rusak dan lain-lain.",
            "Kenyamanan Fasilitas: Kantin", 5f, "Sangat baik. Dari segi fasilitas tempat duduk, kipas maupun ketersediaan makanan yang bervariasi."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Inisialisasi SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool(1, android.media.AudioManager.STREAM_MUSIC, 0)

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) isSoundLoaded = true
        }
        soundId = soundPool.load(this, R.raw.sound_click, 1)

        // Inisialisasi Views
        val txtFakultasTitle = findViewById<TextView>(R.id.txtFakultasTitle)
        val imgInterview = findViewById<ImageView>(R.id.imgInterview)
        val imgDokumentasiWawancara = findViewById<ImageView>(R.id.imgDokumentasiWawancara)
        val ratingKelas = findViewById<RatingBar>(R.id.ratingKelas)
        val txtDetailKelas = findViewById<TextView>(R.id.txtDetailKelas)
        val txtFasilitasLabel = findViewById<TextView>(R.id.txtFasilitasLabel)
        val ratingFasilitas = findViewById<RatingBar>(R.id.ratingFasilitas)
        val txtDetailFasilitas = findViewById<TextView>(R.id.txtDetailFasilitas)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)

        fun renderData(index: Int) {
            val data = dataLaporan[index]
            txtFakultasTitle.text = "Laporan: ${data.namaFakultas}"
            imgInterview.setImageResource(data.fotoResId)
            imgDokumentasiWawancara.setImageResource(data.fotoInterviewResId)
            ratingKelas.rating = data.skorKelas

            txtDetailKelas.text = "Hasil Wawancara Responden:\n${data.detailKelas}"

            txtFasilitasLabel.text = data.labelFasilitas
            ratingFasilitas.rating = data.skorFasilitas

            txtDetailFasilitas.text = "Hasil Wawancara Responden:\n${data.detailFasilitas}"

            if (index == dataLaporan.size - 1) {
                btnNext.visibility = android.view.View.GONE
            } else {
                btnNext.visibility = android.view.View.VISIBLE
            }
        }

        renderData(currentIndex)

        btnNext.setOnClickListener {
            if (isSoundLoaded) soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            if (currentIndex < dataLaporan.size - 1) {
                currentIndex++
                renderData(currentIndex)
            }
        }

        btnBack.setOnClickListener {
            if (isSoundLoaded) soundPool.play(soundId, 1f, 1f, 0, 0, 1f)

            if (currentIndex == 0) {
                finish()
            } else {
                currentIndex--
                renderData(currentIndex)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}