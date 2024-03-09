package com.example.aviatickets.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aviatickets.R
import com.example.aviatickets.adapter.OfferListAdapter
import com.example.aviatickets.databinding.FragmentOfferListBinding
import com.example.aviatickets.model.entity.Offer
import com.example.aviatickets.model.network.ApiClient.service

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfferListFragment : Fragment() {

    companion object {
        fun newInstance() = OfferListFragment()
    }

    private var _binding: FragmentOfferListBinding? = null
    private val binding
        get() = _binding!!

    private val adapter = OfferListAdapter();
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service.getOfferList().enqueue(object : Callback<List<Offer>> {
            override fun onResponse(call: Call<List<Offer>>, response: Response<List<Offer>>) {
                if (response.isSuccessful) {

                    val offerList = response.body() ?: listOf()
                    if (offerList != null) {
                        adapter.updateItems(offerList)
                    }
                    setupUI(offerList)
                }
            }

            override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
            }
        })

    }


    private fun setupUI(originalList: List<Offer>) {
        with(binding) {
            offerList.adapter = adapter

            sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                val sortedList = when (checkedId) {
                    R.id.sort_by_price -> originalList.sortedBy { it.price }
                    R.id.sort_by_duration -> originalList.sortedBy { it.flight.duration }
                    else -> originalList
                }
                adapter.updateItems(sortedList)
            }
        }
    }
}