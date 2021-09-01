package id.uviwi.notes.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import id.uviwi.notes.R
import id.uviwi.notes.adapter.Adapter
import id.uviwi.notes.databinding.ActivityMainBinding
import id.uviwi.notes.databinding.CustomAppbarBinding
import id.uviwi.notes.databinding.ItemBinding
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
    private var itemSelection : ArrayList<Note> = ArrayList(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        _toolbar = CustomAppbarBinding.bind(_binding.root)
        setContentView(_binding.root)
        _binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
        _toolbar.apply {
            constraintDelete.setOnClickListener {
                if (itemSelection.size > 0){
                    dialogBuilderDelete(itemSelection)
                }
            }
            constraintBack.setOnClickListener {
                onResume()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupObserver()
    }
    private fun setupUI() {
        itemSelection.clear()
        _toolbar.apply {
            constraintDelete.visibility = View.GONE
            tvTitle.text = getString(R.string.app_name)
            constraintSave.visibility = View.GONE
            constraintBack.visibility = View.GONE
        }
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

    private fun dialogBuilderDelete(ids : ArrayList<Note>){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Deleting Note ?")
        builder.setMessage("This action cannot be recovered !")
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            delete(ids)
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun delete(ids : ArrayList<Note>){
        model.deleteNote(ids).observe(this, {
            when(it.status){
                State.LOADING -> {
                    Toast.makeText(this, "Loading ...", Toast.LENGTH_SHORT).show()
                }
                State.SUCCESS -> {
                    onResume()
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
    private fun updateTitle(view: ItemBinding? = null){
        if (itemSelection.size == 0){
            itemSelection.clear()
            _toolbar.apply {
                constraintBack.visibility = View.GONE
                constraintDelete.visibility = View.GONE
                tvTitle.text = getString(R.string.app_name)
            }
            view?.ivCheck?.visibility = View.GONE
        }else{
            val itemSize : Int = itemSelection.size
            val textTitle = "$itemSize ${getString(R.string.selection)}"
            _toolbar.tvTitle.text = textTitle
        }
    }
    override fun onClick(note: Note, position: Int, view: ItemBinding) {
        if (itemSelection.size > 0){
            if (itemSelection.contains(note)){
                view.ivCheck.visibility = View.INVISIBLE
                itemSelection.remove(note)
            }else{
                itemSelection.add(note)
                view.ivCheck.visibility = View.VISIBLE
            }
            updateTitle(view)
        }else{
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("detail", note)
            startActivity(intent)
        }
    }

    override fun onLongClick(note: Note, position: Int, view: ItemBinding) {
        itemSelection = ArrayList()
        itemSelection.add(note)
        updateTitle()
        view.ivCheck.visibility = View.VISIBLE
        _toolbar.apply {
            constraintBack.visibility = View.VISIBLE
            constraintDelete.visibility = View.VISIBLE
        }

    }
}