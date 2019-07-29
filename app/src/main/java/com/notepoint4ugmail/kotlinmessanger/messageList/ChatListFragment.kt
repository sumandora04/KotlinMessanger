package com.notepoint4ugmail.kotlinmessanger.messageList


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.notepoint4ugmail.kotlinmessanger.R
import com.notepoint4ugmail.kotlinmessanger.databinding.FragmentChatListBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class ChatListFragment : Fragment() {

    private lateinit var viewModel: ChatListViewModel
    private lateinit var binding: FragmentChatListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatListBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(ChatListViewModel::class.java)
        binding.chatListViewModel = viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        activity?.actionBar?.title = "Messenger"

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_new_message->{
                Navigation.findNavController(view!!).navigate(ChatListFragmentDirections.actionChatListFragmentToNewMessageFragment())
            }

            R.id.menu_sign_out->{
                viewModel.onSignOut()
                Navigation.findNavController(view!!).navigate(ChatListFragmentDirections.actionChatListFragmentToLoginActivity())
                (activity)?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
