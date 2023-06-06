package com.mixlr.panos.studentregister

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mixlr.panos.studentregister.databinding.ActivityMainBinding
import com.mixlr.panos.studentregister.db.Student
import com.mixlr.panos.studentregister.db.StudentDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentRecycleViewAdapter

    private lateinit var selectedStudent: Student
    private var listItemClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = StudentDatabase.getInstance(application).studentDao()
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(StudentViewModel::class.java)

        binding.btnSave.setOnClickListener {
            if (listItemClicked) {
                updateStudentData()
            } else {
                saveStudentData()
                clearInput()
            }
        }

        binding.btnClear.setOnClickListener {
            if (listItemClicked) {
                deleteStudentData()
                clearInput()
            } else {
                clearInput()
            }
        }

        initRecyclerView()
    }

    private fun saveStudentData() {
        viewModel.insertStudent(
            Student(
                0,
                binding.etEmail.text.toString(),
                binding.etEmail.text.toString()
            )
        )
    }

    private fun clearInput() {
        binding.etName.setText("")
        binding.etEmail.setText("")
    }

    private fun initRecyclerView() {
        binding.rvStudent.layoutManager = LinearLayoutManager(this)
        adapter = StudentRecycleViewAdapter {
            selectedItem: Student -> listItemClicked(selectedItem)
        }
        binding.rvStudent.adapter = adapter

        displayStudentsList()
    }

    private fun displayStudentsList() {
        viewModel.students.observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(student: Student) {
        selectedStudent = student
        binding.btnSave.text = "Update"
        binding.btnClear.text = "Delete"
        listItemClicked = true
        binding.etName.setText(selectedStudent.name)
        binding.etEmail.setText(selectedStudent.email)
    }

    private fun updateStudentData() {
        viewModel.updateStudent(
            Student(
                selectedStudent.id,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )

        binding.btnSave.text = "Save"
        binding.btnClear.text = "Clear"
        listItemClicked = false
    }

    private fun deleteStudentData() {
        viewModel.deleteStudent(
            Student(
                selectedStudent.id,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )

        binding.btnSave.text = "Save"
        binding.btnClear.text = "Clear"
        listItemClicked = false
    }
}