package com.notepoint4ugmail.kotlinmessanger.messageList


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.notepoint4ugmail.kotlinmessanger.R
import com.notepoint4ugmail.kotlinmessanger.databinding.FragmentChatListBinding
import com.notepoint4ugmail.kotlinmessanger.model.ChatMessage
import com.notepoint4ugmail.kotlinmessanger.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_list_row.view.*

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

        val adapter = GroupAdapter<ViewHolder>()

        viewModel.latestMessageList.observe(this, Observer {
            it?.let {
                viewModel.chatPartnerDetail.observe(this, Observer {
                    it?.let {
                       // adapter.clear()
                        adapter.add(LatestMessageList(viewModel.latestMessageList.value!!,it))
                    }
                })
            }
        })




        binding.chatListRecyclerView.adapter = adapter
        binding.chatListRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            val row = item as LatestMessageList
            val chatPartner = row.userDetail
            chatPartner.let {
                this.findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToChatLogFragment(chatPartner))
            }
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                Navigation.findNavController(view!!)
                    .navigate(ChatListFragmentDirections.actionChatListFragmentToNewMessageFragment())
            }

            R.id.menu_sign_out -> {
                viewModel.onSignOut()
                Navigation.findNavController(view!!)
                    .navigate(ChatListFragmentDirections.actionChatListFragmentToLoginActivity())
                (activity)?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

class LatestMessageList(val chatMessage: ChatMessage, val userDetail: User) : Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.message_list_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.user_name.text = userDetail.userName
        viewHolder.itemView.latest_message.text = chatMessage.text

        Glide.with(viewHolder.itemView.user_profile_image)
            .load(userDetail.profileImageUrl)
            .into(viewHolder.itemView.user_profile_image)
    }

}
