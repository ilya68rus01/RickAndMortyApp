package com.example.rickandmortyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.rickandmortyapp.databinding.ActivityMainBinding
import com.example.rickandmortyapp.databinding.FragmentGeneralBinding
import com.example.rickandmortyapp.models.CharacterModel
import com.example.rickandmortyapp.models.ListCharacterModel
import com.example.rickandmortyapp.repository.RemoteRepository
import com.example.rickandmortyapp.repository.RemoteRepositoryImpl
import java.lang.NullPointerException

class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding get() = _binding ?: throw NullPointerException("Binding is not initialized")

    val repository: RemoteRepository = RemoteRepositoryImpl(this::repositoryCallback)

    val adapter: CharacterAdapter = CharacterAdapter(this::callbackData)

    fun repositoryCallback(model: CharacterModel) {
        adapter.setData(model.results)
    }

    fun callbackData(callback: ListCharacterModel) {

        val secondFragment = SecondFragment()
        secondFragment.apply {
            arguments = bundleOf("key" to callback)
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, secondFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        repository.request()

        binding.recycler.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}