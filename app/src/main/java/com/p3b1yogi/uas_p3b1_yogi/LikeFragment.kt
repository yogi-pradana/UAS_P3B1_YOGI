package com.p3b1yogi.uas_p3b1_yogi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.p3b1yogi.uas_p3b1_yogi.database.Calon
import com.p3b1yogi.uas_p3b1_yogi.database.CalonDao
import com.p3b1yogi.uas_p3b1_yogi.database.CalonRoomDb
import com.p3b1yogi.uas_p3b1_yogi.databinding.FragmentLikeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikeFragment : Fragment() {
    private lateinit var binding: FragmentLikeBinding
    private lateinit var calonDao: CalonDao
    private lateinit var adapter: CalonUserAdapter
    private var selectedCalons: ArrayList<CalonUserData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Room Database dan DAO
        val db = CalonRoomDb.getDatabase(requireContext())
        calonDao = db!!.localDao()!!

        // Setup RecyclerView
        adapter = CalonUserAdapter(selectedCalons)
        binding.rvLike.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLike.adapter = adapter

        // Ambil data dari Room Database
        fetchSelectedCalons()
    }

    private fun fetchSelectedCalons() {
        CoroutineScope(Dispatchers.IO).launch {
            val calons = calonDao.allPostsLocal().value ?: listOf()
            selectedCalons.clear()

            // Ubah data dari Calon menjadi CalonUserData
            for (calon in calons) {
                selectedCalons.add(
                    CalonUserData(
                        name = calon.namaCalon,
                        age = calon.ageCalon,
                        hobby = calon.hobbyCalon,
                        deskripsi = calon.deskripsiCalon,
                        imageUrl = calon.imgCalon
                    )
                )
            }

            // Update RecyclerView di thread utama
            requireActivity().runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }
}
