package org.jdc.template.ux.directory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vikingsen.inject.viewmodel.savedstate.SavedStateViewModelFactory
import kotlinx.coroutines.flow.collect
import me.eugeniomarletti.extras.bundle.BundleExtra
import me.eugeniomarletti.extras.bundle.base.Int
import org.jdc.template.R
import org.jdc.template.databinding.DirectoryBinding
import org.jdc.template.ext.getScrollPosition
import org.jdc.template.inject.Injector
import org.jdc.template.ui.menu.CommonMenu
import javax.inject.Inject


class DirectoryFragment : Fragment() {
    @Inject
    lateinit var commonMenu: CommonMenu
    @Inject
    lateinit var viewModelFactoryFactory: SavedStateViewModelFactory.Factory

    private val viewModel by viewModels<DirectoryViewModel> { viewModelFactoryFactory.create(this, null) }
    private lateinit var binding: DirectoryBinding
    private val adapter by lazy { DirectoryAdapter(viewModel) }

    init {
        Injector.get().inject(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.directory, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = this@DirectoryFragment.viewModel
        binding.lifecycleOwner = this@DirectoryFragment

        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.setTitle(R.string.app_name)

        savedInstanceState?.let { restoreState(it) }

        callIndividuals()

        setupViewModel()
    }

    private fun callIndividuals() {
        lifecycleScope.launchWhenStarted {
            viewModel.callIndividuals()
        }
    }

    private fun setupViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.getDirectoryList().collect { list ->
                adapter.submitList(list)
            }
        }

        // Events
        lifecycleScope.launchWhenStarted {
            for (event in viewModel.eventChannel) {
                when (event) {
                    is DirectoryViewModel.Event.NewIndividualEvent -> showNewIndividual()
                    is DirectoryViewModel.Event.ShowIndividualEvent -> showIndividual(event.individualId)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        if(resources.getBoolean(R.bool.isPhone)) {
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        }else{
           binding.recyclerView.layoutManager = GridLayoutManager(activity, 3)
        }
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.directory_menu, menu)
        inflater.inflate(R.menu.common_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return commonMenu.onOptionsItemSelected(findNavController(), item) || super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        with(SaveStateOptions) {
            if (::binding.isInitialized) { // onSaveInstanceState may be called before onCreateView
                bundle.scrollPosition = binding.recyclerView.getScrollPosition()
            }
        }
    }

    private fun showNewIndividual() {
        findNavController().navigate(DirectoryFragmentDirections.newIndividual())
    }

    private fun showIndividual(individualId: Long) {
        val directions = DirectoryFragmentDirections.showIndividual(individualId)
        findNavController().navigate(directions)
    }

    private fun restoreState(bundle: Bundle) {
        with(SaveStateOptions) {
            binding.recyclerView.scrollToPosition(bundle.scrollPosition ?: 0)
        }
    }

    object SaveStateOptions {
        var Bundle.scrollPosition by BundleExtra.Int()
    }
}
