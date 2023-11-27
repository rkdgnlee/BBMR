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


class Senior_Fragment_Tab_Recommend : Fragment(), ItemClickListener {
    override fun onItemClick(item: Senior_TakeOutVO) {
    }

    private lateinit var viewModel: MenuListViewModel
    private lateinit var adapter: SeniorTakeOutAdapter
    private lateinit var rvRecommend: RecyclerView

    var menuIndex = 6

    // 특정 위치로 스크롤하는 함수
    fun scrollToPosition(position: Int) {
        rvRecommend.post {
            rvRecommend.smoothScrollToPosition(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(MenuListViewModel::class.java)

        val view = inflater.inflate(R.layout.frag_senior_tab_recommend, container, false)
        rvRecommend = view.findViewById(R.id.rvRecommend)

        // RecyclerView 어댑터 초기화
        adapter = SeniorTakeOutAdapter(
            requireContext(),
            R.layout.frag_senior_list,
            arrayListOf(),
            this,
            parentFragmentManager
        )
        rvRecommend.adapter = adapter
        rvRecommend.layoutManager = GridLayoutManager(requireContext(), 3)


        // menuList1 LiveData 어댑터 업데이트
        viewModel.menuList1.observe(viewLifecycleOwner) { menuList ->
            adapter.updateList(menuList)
        }

        return view
    }


    // menuList1로 전환하는 함수
    fun switchToMenuList1() {
        viewModel.menuList1.value?.let { menuList1 ->
            adapter.updateList(menuList1)
        }
    }

    // menuList2로 전환하는 함수
    fun switchToMenuList2() {
        viewModel.menuList2.value?.let { menuList2 ->
            adapter.updateList(menuList2)
        }
    }

}


