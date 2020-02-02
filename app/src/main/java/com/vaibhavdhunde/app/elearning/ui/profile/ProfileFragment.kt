package com.vaibhavdhunde.app.elearning.ui.profile

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentProfileBinding
import com.vaibhavdhunde.app.elearning.ui.login.LoginActivity
import com.vaibhavdhunde.app.elearning.ui.main.MainViewModel
import com.vaibhavdhunde.app.elearning.util.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ProfileFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileViewModel = obtainViewModel(ProfileViewModel::class.java, factory)

        binding = FragmentProfileBinding.bind(view).apply {
            viewmodel = profileViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupInputField()
        setupEvents()
        setupNavigation()
        loadProfile()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                logoutUser()
                true
            }
            R.id.menu_deactivate -> {
                binding.viewmodel?.showDeactivateAccountAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupInputField() {
        input_name.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateProfileName()
                true
            } else {
                false
            }
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            if (message is Int) {
                fragment_profile.snackbar(message)
            } else if (message is String) {
                fragment_profile.snackbar(message)
            }
        })

        binding.viewmodel?.closeSoftKeyboardEvent?.observe(viewLifecycleOwner, EventObserver {
            closeSoftKeyboard()
        })

        binding.viewmodel?.deactivateAccountAlertEvent?.observe(viewLifecycleOwner, EventObserver {
            showDeactivateAccountAlertDialog()
        })

        binding.viewmodel?.dataAvailable?.observe(viewLifecycleOwner, Observer { isAvailable ->
            setHasOptionsMenu(isAvailable)
        })

        binding.viewmodel?.nameUpdatedEvent?.observe(viewLifecycleOwner, EventObserver {
            activity?.let {
                val mainViewModel = ViewModelProvider(it, factory)[MainViewModel::class.java]
                mainViewModel.loadUser()
            }
        })
    }

    private fun setupNavigation() {
        binding.viewmodel?.changePasswordEvent?.observe(viewLifecycleOwner, EventObserver {
            navigateToChangePasswordFragment()
        })

        binding.viewmodel?.loginEvent?.observe(viewLifecycleOwner, EventObserver {
            navigateToLoginActivity()
        })
    }

    private fun loadProfile() {
        binding.viewmodel?.loadProfile()
    }

    private fun updateProfileName() {
        binding.viewmodel?.updateProfileName()
    }

    private fun showDeactivateAccountAlertDialog() {
        AlertDialog.Builder(context!!).apply {
            setTitle(R.string.dialog_title_confirm_action)
            setMessage(R.string.dialog_msg_confirm_action)
            setNegativeButton(R.string.option_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.option_deactivate) { _, _ ->
                deactivateAccount()
            }
            show()
        }
    }

    private fun deactivateAccount() {
        binding.viewmodel?.deactivateAccount()
    }

    private fun logoutUser() {
        binding.viewmodel?.logoutUser()
    }

    private fun navigateToChangePasswordFragment() {
        val action = ProfileFragmentDirections.actionChangePassword()
        findNavController().navigate(action)
    }

    private fun navigateToLoginActivity() {
        startFreshActivity(LoginActivity::class.java)
    }

}
