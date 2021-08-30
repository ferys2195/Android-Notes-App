package id.uviwi.notes.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import id.uviwi.notes.adapter.Adapter
import id.uviwi.notes.databinding.ActivityMainBinding
import id.uviwi.notes.databinding.CustomAppbarBinding
import id.uviwi.notes.model.Note
import id.uviwi.notes.utils.INote
import id.uviwi.notes.utils.State
import id.uviwi.notes.viewmodel.NoteViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), INote {
    private lateinit var _binding : ActivityMainBinding
    private lateinit var _toolbar : CustomAppbarBinding
    private lateinit var adapter: Adapter
    private val model : NoteViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        _toolbar = CustomAppbarBinding.bind(_binding.root)
        setContentView(_binding.root)
        _binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupObserver()
    }
    private fun setupUI() {
        _toolbar.constraintSave.visibility = View.GONE
        _toolbar.constraintBack.visibility = View.GONE
        adapter = Adapter(arrayListOf(), this)
        _binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        _binding.recyclerView.adapter = adapter
    }
    private fun setupObserver(){
        model.getNote().observe(this, {
            when(it.status){
                State.LOADING -> {
                    _binding.recyclerView.visibility = View.GONE
                }
                State.SUCCESS -> {
                    _binding.recyclerView.visibility = View.VISIBLE
                    it.data?.let { it1 -> renderList(it1) }
                }
                State.ERROR -> {
                    Toast.makeText(this, "an Erro ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun dialogBuilderDelete(uuid : Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Deleting Note ?")
        builder.setMessage("This action cannot be recovered !")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            delete(uuid)
        }

        builder.setNegativeButton(android.R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun delete(uuid : Int){
        model.deleteNote(uuid).observe(this, {
            when(it.status){
                State.LOADING -> {
                    Toast.makeText(this, "Loading ...", Toast.LENGTH_SHORT).show()
                }
                State.SUCCESS -> {
                    Toast.makeText(this, "Delete Succesfully", Toast.LENGTH_SHORT).show()
                }
                State.ERROR -> {
                    Toast.makeText(this, "an Erro ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun renderList(it : List<Note>){
        adapter.addAll(it)
        adapter.notifyDataSetChanged()
    }

    override fun onClick(note: Note) {
        val intent = Intent(this, CreateActivity::class.java)
        intent.putExtra("detail", note)
        startActivity(intent)
    }

    override fun onLongClick(note: Note) {
        // todo : manage long click with multi selection for delete
        /*
        * todo
        *  example : when click 1 item, the action using long click for selection
        *  after that when the selection > 0, the action using onclick for selection increse
        * */
        _toolbar.constraintDelete.visibility = View.VISIBLE
        _toolbar.constraintDelete.setOnClickListener {
            dialogBuilderDelete(note.id)
        }
    }
}