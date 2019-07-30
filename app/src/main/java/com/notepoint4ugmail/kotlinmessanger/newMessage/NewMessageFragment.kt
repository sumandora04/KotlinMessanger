package com.notepoint4ugmail.kotlinmessanger.newMessage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.notepoint4ugmail.kotlinmessanger.databinding.FragmentNewMessageBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class NewMessageFragment : Fragment() {
    private lateinit var binding: FragmentNewMessageBinding
    private lateinit var viewModel: NewMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewMessageBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(NewMessageViewModel::class.java)
        binding.newMessageViewModel = viewModel

        binding.lifecycleOwner = this

        val adapter = NewMessageAdapter(NewMessageAdapter.NewUserClickListener{
//            Toast.makeText(context,it.userName,Toast.LENGTH_SHORT).show()
            this.findNavController().navigate(NewMessageFragmentDirections.actionNewMessageFragmentToChatLogFragment(it))
        })
        viewModel.userList.observe(this, Observer {
            adapter.submitList(it)
        })

        binding.userRecyclerView.adapter = adapter
        return binding.root
    }


}
