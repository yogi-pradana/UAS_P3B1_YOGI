package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// adapter `CalonUserAdapter` digunakan untuk menghubungkan data calon pada bagian pengguna (user) dengan tampilan RecyclerView
// data calon ditampilkan dalam item.xml, dan setiap item menampilkan nama dan gambar calon
// adapter juga menangani klik pada setiap item, sehingga ketika pengguna mengklik calon
// akan diarahkan ke DetailCalonActivity dengan membawa informasi detail calon

class CalonUserAdapter(private val filmUserList: ArrayList<CalonUserData>) : RecyclerView.Adapter<CalonUserAdapter.CalonUserViewHolder>() {
    // ViewHolder berfungsi untuk merepresentasikan setiap item dalam RecyclerView
    class CalonUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameUser: TextView = itemView.findViewById(R.id.nama_calon)
        val imageUser: ImageView = itemView.findViewById(R.id.image_calon)
    }

    // onCreateViewHolder digunakan untuk membuat instance dari ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalonUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return CalonUserViewHolder(itemView)
    }

    // onBindViewHolder menghubungkan data film dengan ViewHolder untuk setiap item
    override fun onBindViewHolder(holder: CalonUserViewHolder, position: Int) {
        val currentItem = filmUserList[position]
        holder.nameUser.text = currentItem.name


        // menggunakan Glide untuk memuat gambar dari URL ke dalam ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.imageUser)

        // menangani klik pada setiap item, mengarahkan pengguna ke DetailCalonActivity dengan membawa informasi detail calon
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,DetailCalonActivity::class.java)
            intent.putExtra("Nama",currentItem.name)
            intent.putExtra("Usia",currentItem.age)
            intent.putExtra("Hobby",currentItem.hobby)
            intent.putExtra("Deskripsi",currentItem.deskripsi)
            intent.putExtra("ImageUrl",currentItem.imageUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    // getItemCount mengembalikan jumlah total item dalam RecyclerView
    override fun getItemCount() = filmUserList.size
}