package com.mixlr.panos.studentregister

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mixlr.panos.studentregister.databinding.ListItemBinding
import com.mixlr.panos.studentregister.db.Student

class StudentRecycleViewAdapter(
    private val clickListener: (Student) -> Unit
) : RecyclerView.Adapter<StudentViewHolder>() {
    private val studentList = ArrayList<Student>()
    private lateinit var binding: ListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StudentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position], clickListener)
    }

    fun setList(students: List<Student>) {
        studentList.clear()
        studentList.addAll(students)
    }
}

class StudentViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(student: Student, clickListener: (Student) -> Unit) {
        binding.apply {
            tvName.text = student.name
            tvEmail.text = student.email

            root.setOnClickListener {
                clickListener(student)
            }
        }
    }
}