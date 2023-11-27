package com.example.bbmr_project.Senior_Fragment.seniorAdapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bbmr_project.Dialog.Senior_MenuDialog
import com.example.bbmr_project.R
import com.example.bbmr_project.Senior_Fragment.Senior_Fragment_Tab_Dessert
import com.example.bbmr_project.VO.Senior_TakeOutVO


interface ItemClickListener {
    fun onItemClick(item: Senior_TakeOutVO)
}

// RecyclerView Adapter 클래스 정의
class SeniorTakeOutAdapter(
    val context: Context, val layout: Int, val menuList: ArrayList<Senior_TakeOutVO>,
    private val itemClickListener: ItemClickListener?,
    private val fragmentManager: FragmentManager,


    ) : RecyclerView.Adapter<SeniorTakeOutAdapter.ViewHolder>() {


    // LayoutInflater를 이용하여 레이아웃을 인플레이트하기 위한객체 초기화
    val inflater: LayoutInflater = LayoutInflater.from(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgS: ImageView = view.findViewById(R.id.imgS)
        val tvNameS: TextView = view.findViewById(R.id.tvNameS)
        val tvPriceS: TextView = view.findViewById(R.id.tvPriceS)
    }

    fun updateList(newList: List<Senior_TakeOutVO>) {
        menuList.clear()
        menuList.addAll(newList)
        notifyDataSetChanged()
    }


    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder에 데이터 바인딩
    // 메뉴 사진, 이름, 가격 설정
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(menuList[position].simg.toString()).into(holder.imgS)

        holder.tvNameS.text = menuList[position].sname
        // 기본값을 1000단위로 나누는 코드
        val basicPrice = String.format("%,d 원", menuList[position].sprice)
        holder.tvPriceS.text = basicPrice
//        holder.imgS.setImageResource(menuList[position].simg)

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(menuList[position])
            // MenuDialog에 값을 보내주기 위한 코드
            val seniorMenuDialog = Senior_MenuDialog()
            val bundle = Bundle().apply{
                putParcelable("seniorTakeOutVO", menuList[position])
            }
            seniorMenuDialog.arguments = bundle
            seniorMenuDialog.show(fragmentManager, "seniorDialog")
        }
    }

    // 데이터 아이템 개수 반환
    override fun getItemCount(): Int {
        return menuList.size
    }

}
