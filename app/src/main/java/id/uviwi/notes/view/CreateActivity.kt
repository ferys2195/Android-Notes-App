package id.uviwi.notes.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import id.uviwi.notes.databinding.ActivityCreateBinding
import id.uviwi.notes.databinding.CustomAppbarBinding
import id.uviwi.notes.model.Note
import id.uviwi.notes.utils.TextFormatter
import id.uviwi.notes.utils.State
import id.uviwi.notes.utils.TextViewUndoRedo
import id.uviwi.notes.viewmodel.NoteViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class CreateActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityCreateBinding
    private lateinit var _toolbar: CustomAppbarBinding
    private lateinit var helper: TextViewUndoRedo
    private val model: NoteViewModel by viewModel()
    private var data: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateBinding.inflate(layoutInflater)
        _toolbar = CustomAppbarBinding.bind(_binding.root)
        setContentView(_binding.root)
        data = intent.getParcelableExtra("detail")
        helper = TextViewUndoRedo(_binding.editText)
        setupUI()
        setOnClickListener()
        setRequestFocusable()
        _binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                _toolbar.constraintSave.visibility = View.GONE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty() == true) {
                    _toolbar.constraintSave.visibility = View.VISIBLE
                } else {
                    _toolbar.constraintSave.visibility = View.INVISIBLE
                }
                if (helper.canUndo) {
                    _toolbar.constraintUndo.visibility = View.VISIBLE
                }else {
                    _toolbar.constraintUndo.visibility = View.INVISIBLE
                }
                if (helper.canRedo) {
                    _toolbar.constraintRedo.visibility = View.VISIBLE
                }else {
                    _toolbar.constraintRedo.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setOnClickListener() {
        _toolbar.constraintBack.setOnClickListener { onBackPressed() }
        _toolbar.constraintSave.setOnClickListener { insertNote() }
        _toolbar.constraintUndo.setOnClickListener { helper.undo() }
        _toolbar.constraintRedo.setOnClickListener { helper.redo() }
    }

    private fun setRequestFocusable() {
        _binding.editText.requestFocus()
        _binding.editText.isCursorVisible = true
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(_binding.editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupUI() {
        if (data?.text?.isNotEmpty() == true) {
            _binding.editText.setText(data?.text)
        }
        _binding.tvInfo.text =
            data?.date?.let { TextFormatter.textInfo(it, _binding.editText.length()) }
    }

    private fun insertNote() {
        val noteText = _binding.editText.text.toString()
        val id = data?.id ?: 0
        val note = Note(id, noteText, Date())
        model.insertNote(note).observe(this, {
            when (it.status) {
                State.LOADING -> {
                    Toast.makeText(this, "Please Wait ...", Toast.LENGTH_SHORT).show()
                }
                State.SUCCESS -> {
                    finish()
                }
                State.ERROR -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}