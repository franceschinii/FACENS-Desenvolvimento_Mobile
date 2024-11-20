package com.example.studentregister.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregister.R
import com.example.studentregister.model.Student

class StudentAdapter(
    private var studentList: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int = studentList.size

    fun updateStudents(newStudentList: List<Student>) {
        studentList = newStudentList
        notifyDataSetChanged()
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvRa: TextView = itemView.findViewById(R.id.tvRa)

        fun bind(student: Student) {
            tvName.text = student.name
            tvRa.text = "RA: ${student.ra}"

            itemView.setOnClickListener {
                onItemClick(student)
            }
        }
    }
}
