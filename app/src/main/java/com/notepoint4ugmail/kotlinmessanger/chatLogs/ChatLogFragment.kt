package com.notepoint4ugmail.kotlinmessanger.chatLogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.notepoint4ugmail.kotlinmessanger.R
import com.notepoint4ugmail.kotlinmessanger.databinding.FragmentChatLogBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ChatLogFragment : Fragment() {
    private lateinit var binding: FragmentChatLogBinding
    private lateinit var viewModel: ChatLogViewModel

    companion object{
        var imageUrlCurrentUser:String? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatLogBinding.inflate(inflater)

        val application = requireNotNull(activity!!).application
        val userDetail = ChatLogFragmentArgs.fromBundle(arguments!!).selectedUserDetail

        // activity?.title = chatPartnerDetail.userName
        this.findNavController().graph.label = userDetail.userName

        val modelFactory = ChatLogFactory(userDetail, application)
        viewModel = ViewModelProviders.of(this, modelFactory).get(ChatLogViewModel::class.java)

        binding.chatLogViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.currentUser.observe(this, Observer {
            imageUrlCurrentUser = it.profileImageUrl
        })

        viewModel.sendMessageSuccess.observe(this, Observer {
            if (it) {
                binding.newMessageEditText.text.clear()
            }
        })

        val adapter = GroupAdapter<ViewHolder>()
        viewModel.messageList.observe(this, Observer {
            it?.let {
                if (it.toId==FirebaseAuth.getInstance().uid) {
                    adapter.add(ChatFromItems(it.text, userDetail.profileImageUrl))
                }else {
                    adapter.add(ChatToItems(it.text, imageUrlCurrentUser))
                }

                binding.chatLogRecyclerView.scrollToPosition(adapter.itemCount-1)
            }
        })

        binding.sendMessageButton.setOnClickListener {
            val textMessage = binding.newMessageEditText.text.toString()
            if (textMessage.isNotEmpty()) {
                viewModel.performSendMessage(textMessage)
                viewModel.resetStatus()
            } else {
                return@setOnClickListener
            }
        }

        binding.chatLogRecyclerView.adapter = adapter



        return binding.root
    }


    //Groupie for recyclerView adapter:
    class ChatFromItems(val message: String, val imageUrl:String) : Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.user_message_from.text = message

            Glide.with(viewHolder.itemView.user_image_from)
                .load(imageUrl)
                .into( viewHolder.itemView.user_image_from)

        }

    }

    class ChatToItems(val message: String, val imageUrl:String?) : Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.user_message_to.text = message

            Glide.with(viewHolder.itemView.user_image_to)
                .load(imageUrl)
                .into( viewHolder.itemView.user_image_to)
        }

    }
}
