package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

// kelas `CalonAdminAdapter` adalah adaptor RecyclerView untuk tampilan daftar calon pada bagian admin
// adaptor ini mengelola item-item dalam daftar dan mengatur tampilan masing-masing item
class CalonAdminAdapter(private val CalonAdminList: ArrayList<CalonAdminData>) : RecyclerView.Adapter<CalonAdminAdapter.CalonAdminViewHolder>() {

    // kelas `CalonAdminViewHolder` merepresentasikan tampilan item pada daftar film admin
    class CalonAdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nama_calon_admin)
        val age: TextView = itemView.findViewById(R.id.age_calon_admin)
        val hobby: TextView = itemView.findViewById(R.id.hobby_calon_admin)
        val deskripsi: TextView = itemView.findViewById(R.id.deskripsi_calon_admin)
        val image: ImageView = itemView.findViewById(R.id.image_film_admin)
    }

    // metode yang dipanggil ketika RecyclerView memerlukan tampilan baru untuk mewakili item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalonAdminViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_calon, parent, false)
        return CalonAdminViewHolder(itemView)
    }

    // metode yang dipanggil untuk menetapkan data film pada tampilan item yang ditangani oleh `FilmAdminViewHolder`
    override fun onBindViewHolder(holder: CalonAdminViewHolder, position: Int) {
        val currentItem = CalonAdminList[position]

        // menetapkan nilai dari objek `CalonAdminData` ke tampilan masing-masing item
        holder.name.setText(currentItem.name)
        holder.age.setText(currentItem.age)
        holder.hobby.setText(currentItem.hobby)
        holder.deskripsi.setText(currentItem.deskripsi)

        holder.name.text = currentItem.name

        // menggunakan Glide untuk memuat gambar dari URL ke dalam ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.image)

        // menangani aksi ketika tombol edit ditekan
        holder.itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener{
            val intent = Intent(holder.itemView.context, AdminEditActivity::class.java)
            val currentItem = CalonAdminList[position]
            intent.putExtra("title", currentItem.name)
            intent.putExtra("director", currentItem.age)
            intent.putExtra("durasi", currentItem.hobby)
            intent.putExtra("sinopsis", currentItem.deskripsi)
            intent.putExtra("imgId", currentItem.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

        // menangani aksi ketika tombol hapus ditekan
        holder.itemView.findViewById<ImageButton>(R.id.btn_hapus).setOnClickListener{
            val itemToDelete = Uri.parse(CalonAdminList[position].imageUrl.toString()).lastPathSegment?.removePrefix("images/")

            // menghapus item dari daftar
            CalonAdminList.removeAt(position)

            // memberi tahu adaptor tentang perubahan data
            notifyDataSetChanged()

            // menghapus data yang sesuai dari Realtime Database
            deleteItemFromDatabase(itemToDelete.toString())
        }

    }

    // metode untuk menghapus item dari Firebase Storage dan Realtime Database berdasarkan ID gambar
    private fun deleteItemFromDatabase(imgId: String) {
        // referensi ke Firebase Storage
        val storageReference = FirebaseStorage.getInstance().getReference("images").child(imgId)

        // menghapus gambar dari Firebase Storage
        storageReference.delete().addOnSuccessListener {
            // jika gambar dihapus dengan sukses, hapus juga data yang sesuai dari Realtime Database
            val database = FirebaseDatabase.getInstance().getReference("Film")
            database.child(imgId).removeValue()
                .addOnCompleteListener {
                }
                .addOnFailureListener {
                }
        }.addOnFailureListener {
        }
    }



    // metode yang memberikan jumlah item dalam daftar
    override fun getItemCount() = CalonAdminList.size
}