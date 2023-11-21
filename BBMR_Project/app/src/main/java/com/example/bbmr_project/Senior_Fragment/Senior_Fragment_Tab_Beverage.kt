package com.example.bbmr_project.Senior_Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bbmr_project.Menu.MenuListViewModel
import com.example.bbmr_project.R
import com.example.bbmr_project.Senior_Fragment.seniorAdapters.ItemClickListener
import com.example.bbmr_project.Senior_Fragment.seniorAdapters.SeniorTakeOutAdapter
import com.example.bbmr_project.VO.Senior_TakeOutVO

class Senior_Fragment_Tab_Beverage : Fragment(), ItemClickListener {

    override fun onItemClick(item: Senior_TakeOutVO) {
    }
    private lateinit var viewModel: MenuListViewModel
    private lateinit var adapter: SeniorTakeOutAdapter
    private lateinit var rvBeverage : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(MenuListViewModel::class.java)

        val view =  inflater.inflate(R.layout.frag_senior_tab_beverage, container, false)
        rvBeverage = view.findViewById(R.id.rvBeverage)

        adapter = SeniorTakeOutAdapter(requireContext(), R.layout.frag_senior_list, arrayListOf(), this, parentFragmentManager)
        rvBeverage.adapter = adapter
        rvBeverage.layoutManager = GridLayoutManager(requireContext(), 3)

        viewModel.menuList3.observe(viewLifecycleOwner){menuList ->
            adapter.updateList(menuList)

        }

        return view
    }




}