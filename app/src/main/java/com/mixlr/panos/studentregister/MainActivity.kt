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

        binding.apply {
            btnSave.setOnClickListener {
                if (listItemClicked) {
                    updateStudentData()
                } else {
                    saveStudentData()
                    clearInput()
                }
            }

            btnClear.setOnClickListener {
                if (listItemClicked) {
                    deleteStudentData()
                    clearInput()
                } else {
                    clearInput()
                }
            }
        }

        initRecyclerView()
    }

    private fun saveStudentData() {
        binding.apply {
            viewModel.insertStudent(
                Student(
                    0,
                    etName.text.toString(),
                    etEmail.text.toString()
                )
            )
        }
    }

    private fun clearInput() {
        binding.apply {
            etName.setText("")
            etEmail.setText("")
        }
    }

    private fun initRecyclerView() {
        adapter = StudentRecycleViewAdapter {
                selectedItem: Student -> listItemClicked(selectedItem)
        }
        val mainActivity = this
        binding.apply {
            rvStudent.layoutManager = LinearLayoutManager(mainActivity)
            rvStudent.adapter = adapter
        }

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
        listItemClicked = true
        binding.apply {
            btnSave.text = "Update"
            btnClear.text = "Delete"
            etName.setText(selectedStudent.name)
            etEmail.setText(selectedStudent.email)
        }
    }

    private fun updateStudentData() {
        binding.apply {
            viewModel.updateStudent(
                Student(
                    selectedStudent.id,
                    etName.text.toString(),
                    etEmail.text.toString()
                )
            )

            btnSave.text = "Save"
            btnClear.text = "Clear"
        }

        listItemClicked = false
    }

    private fun deleteStudentData() {
        binding.apply {
            viewModel.deleteStudent(
                Student(
                    selectedStudent.id,
                    etName.text.toString(),
                    etEmail.text.toString()
                )
            )

            btnSave.text = "Save"
            btnClear.text = "Clear"
        }

        listItemClicked = false
    }
}