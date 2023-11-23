package com.example.bbmr_project.Normal_Fragment.adapters

import NormalSelectPayAdapter
import NormalSelectedMenuInfo
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bbmr_project.Dialog.TotalCostListener
//import com.example.bbmr_project.Menu.NormalSelectedMenuInfo
import com.example.bbmr_project.R
class NormalSelectBasketAdapter(
    val context: Context,
    val layout: Int,
    val basketList: MutableList<NormalSelectedMenuInfo>,
    val totalCostListener: TotalCostListener,
    val normalSelectPayAdapter: NormalSelectPayAdapter

) : RecyclerView.Adapter<NormalSelectBasketAdapter.ViewHolder>() {

    val inflater: LayoutInflater = LayoutInflater.from(context)
    private var selectedMenuList: MutableList<NormalSelectedMenuInfo> = mutableListOf()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val basketImg: ImageView = view.findViewById(R.id.basketImg)
        val tvBasketCount: TextView = view.findViewById(R.id.tvBasketCount)
        val btnBasketPlus: Button = view.findViewById(R.id.btnBasketPlus)
        val btnBasketMinus: Button = view.findViewById(R.id.btnBasketMinus)
        val btnBasketCancel: Button = view.findViewById(R.id.btnBasketCancel)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NormalSelectBasketAdapter.ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return NormalSelectBasketAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NormalSelectBasketAdapter.ViewHolder, position: Int) {
        val selectedItem = basketList[position]

        holder.basketImg.setImageResource(selectedItem.menuImg)
        holder.tvBasketCount.text = selectedItem.tvCount.toString()

        // btnBasketPlus 클릭 시
        holder.btnBasketPlus.setOnClickListener {
            // tvBasketCount 값을 증가
            selectedItem.tvCount++
            notifyItemChanged(position)

            // NormalSelectPayAdapter를 업데이트
            normalSelectPayAdapter.updateItems(basketList)

            // 현재 총 비용 출력
            val currentTotalCost = calculateTotalCost()
            Log.d("TotalCostUpdated", "Total Cost Increased: $currentTotalCost")

            // 아이템 비용 출력
            Log.d("CalculateItemCost", "Item Cost: ${calculateItemCost(selectedItem)}")

            totalCostListener.onTotalCostUpdated(currentTotalCost,calculateItemCost(selectedItem))
        }

        // btnBasketMinus 클릭 시
        holder.btnBasketMinus.setOnClickListener {
            // tvBasketCount가 2 이상일 때만 감소
            if (selectedItem.tvCount > 1) {
                selectedItem.tvCount--
                notifyItemChanged(position)

                // NormalSelectPayAdapter를 업데이트
                normalSelectPayAdapter.updateItems(basketList)

                // 현재 총 비용 출력
                val currentTotalCost = calculateTotalCost()
                Log.d("TotalCostUpdated", "Total Cost Increased: $currentTotalCost")

                totalCostListener.onTotalCostUpdated(currentTotalCost,calculateItemCost(selectedItem))
            }
        }

        // btnBasketCancel 클릭 시
        holder.btnBasketCancel.setOnClickListener {
            Log.d("ClickEvent", "btnBasketCancel Clicked")
            // 해당 아이템의 정보를 얻어옴
            val selectedItem = basketList[holder.adapterPosition]

            // 출력: 현재 adapterPosition 확인
            Log.d("현재 adapterPosition", "Current adapterPosition: ${holder.adapterPosition}")

            // 해당 아이템의 비용을 총 비용에서 차감
            val itemCost = calculateItemCost(selectedItem)
            val remainingTotalCost = calculateTotalCost() - itemCost

            // 로그로 현재 총 비용 출력
            Log.d("TotalCostUpdated", "Total Cost Decreased: $remainingTotalCost")

            // TotalCostListener에 알림
            totalCostListener.onTotalCostUpdated(remainingTotalCost, itemCost)

            // 해당 아이템을 RecyclerView에서 삭제
            val removedItemPosition = holder.adapterPosition
            removeItemFromBasket(removedItemPosition)

            // NormalSelectPayAdapter에도 삭제 반영
            normalSelectPayAdapter.removeItem(removedItemPosition)
            normalSelectPayAdapter.notifyItemRemoved(removedItemPosition)
            // NormalSelectPayAdapter를 업데이트
            normalSelectPayAdapter.updateItems(basketList)
            normalSelectPayAdapter.notifyDataSetChanged()

            // 전체 취소인 경우 해당 메뉴의 비용을 전체에서 제거
            if (basketList.isEmpty()) {
                Log.d("TotalCostUpdated", "Total Cost Reset: 0")
                totalCostListener.onTotalCostUpdated(0, 0)
            }
            // NormalSelectPayAdapter의 데이터 확인 로그
            Log.d("삭제클릭_데이터확인로그", "PayAdapter Data: ${normalSelectPayAdapter.selectedMenuListProperty}")

        }

    }

    private fun removeItemFromBasket(position: Int) { // 아이템 삭제 메서드
        if (position in 0 until basketList.size) {
            Log.d("바스켓어댑터", "Removing item at position $position")
            basketList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    private fun calculateOptionTvCount(item: NormalSelectedMenuInfo): Int {
        return item.tvCount1 * 500 + item.tvCount2 * 500 + item.tvCount3 * 500 + item.tvCount4 * 500
    }


    override fun getItemCount(): Int {
        return basketList.size
    }

    fun addItem(selectedMenuInfo: NormalSelectedMenuInfo) {
        basketList.add(selectedMenuInfo)
        notifyItemInserted(basketList.size - 1)
    }

    // 현재 장바구니의 총 비용 계산
    private fun calculateTotalCost(): Int {
        var totalCost = 0
        for (item in basketList) {
            // 각 메뉴의 tvCount에 상관없이 price를 곱해서 누적
            totalCost += item.tvCount * (item.price?.replace(",", "")?.replace("원", "")?.toIntOrNull() ?: 0)

            // 옵션 비용도 더해줘야 함
            totalCost += calculateOptionTvCount(item)
        }
        return totalCost
    }




    // 아이템의 비용 계산
    private fun calculateItemCost(item: NormalSelectedMenuInfo): Int {
        // 각 메뉴의 tvCount와 price, optionTvCount를 곱해서 반환 (음수 값이 아닌 0을 반환하도록 수정)
        val menuPrice = (item.price?.replace(",", "")?.replace("원", "")?.toIntOrNull() ?: 0)
        val optionCost = calculateOptionTvCount(item)
        val cost = item.tvCount * (menuPrice + optionCost)
        return if (cost < 0) 0 else cost

        // 디버깅을 위한 로그
        Log.d("CalculateItemCost", "Menu: ${item.name}, TVCount: ${item.tvCount}, MenuPrice: $menuPrice, OptionCost: $optionCost, TotalCost: $cost")

    }

    fun clearItems() {
        selectedMenuList.clear()
        basketList.clear()
    }

}