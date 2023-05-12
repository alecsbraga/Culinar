package com.example.culinar.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinar.R
import com.example.culinar.databinding.FragmentHomeBinding
import com.example.culinar.ui.home.Food.DetailsFood
import com.example.culinar.ui.home.Food.Food
import java.util.*

const val textLorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tincidunt turpis sed ex ultricies, et sollicitudin turpis finibus. Morbi finibus, nunc nec porta ultricies, nibh augue suscipit mi, a facilisis mi risus vel velit. Cras commodo massa sed consequat volutpat. Donec fringilla quam a nunc iaculis porttitor. Phasellus porta posuere odio, et placerat justo sollicitudin posuere. Interdum et malesuada fames ac ante ipsum primis in faucibus. Pellentesque et sagittis urna. Duis ut pulvinar lectus, sed posuere nisl.\n" +
        "\n" +
        "Duis in mollis augue. Nam egestas mauris pretium diam lobortis blandit vel quis nisi. Fusce nec felis massa. Morbi at vestibulum odio. Curabitur sit amet lacus magna. Sed et lacinia quam. Etiam non quam malesuada, semper ante fermentum, pharetra tortor. Nullam ultrices tempus libero, in cursus velit dignissim in. Aliquam et bibendum quam, ac tristique odio.\n" +
        "\n" +
        "Mauris rutrum a risus at lacinia. Sed eget tristique dui. Etiam vel nulla dolor. Aenean venenatis vitae lacus in rutrum. Mauris gravida nibh sed turpis commodo, nec ultrices nisl cursus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam blandit feugiat nisl, ac semper purus fermentum ut. Cras condimentum efficitur scelerisque. Pellentesque commodo, massa vel blandit viverra, nisi urna sollicitudin lorem, id volutpat diam est vel quam. Sed id commodo tellus. Nam vitae turpis ac eros aliquam tempor. Ut malesuada velit vel mi bibendum congue. Nam non risus vel magna porttitor elementum. Duis in libero nec lectus sagittis lobortis. Vivamus suscipit, ex eu gravida faucibus, ex lectus congue augue, in tempor velit neque a tortor.\n" +
        "\n" +
        "Cras feugiat ut magna vel placerat. Mauris venenatis odio vel lobortis iaculis. Nulla molestie nisl vel augue egestas venenatis. Sed vehicula consequat odio in hendrerit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent sodales ipsum ut ante ullamcorper vehicula id et turpis. Nunc porta ante vel tortor consectetur, nec ultricies sem facilisis. Morbi est turpis, lacinia commodo ligula non, luctus sagittis lectus. Vestibulum libero mauris, aliquam sed elit at, tempor facilisis felis. Ut semper laoreet eros, nec malesuada diam rutrum et.\n" +
        "\n" +
        "Donec eu tincidunt augue, eu elementum leo. Proin tellus nulla, commodo id elit id, consectetur consectetur ex. Ut odio augue, scelerisque non ornare nec, vulputate sit amet quam. Ut odio neque, ornare vulputate pulvinar condimentum, venenatis nec libero. Quisque eleifend ligula blandit sapien convallis venenatis. Aenean tempus vulputate tempor. Vivamus cursus dolor risus, vel egestas leo auctor ac. Pellentesque nisi tortor, hendrerit ut dignissim in, sagittis sed dui. Integer eu velit venenatis, accumsan arcu in, porta risus. Morbi sit amet vestibulum erat. Phasellus vehicula congue diam in pulvinar. Morbi vitae est sem. Morbi ornare nisi sed nibh gravida, eget maximus sapien dictum. Nunc diam tortor, tempus vel sodales ac, egestas sed quam."



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodList: ArrayList<Food>
    private lateinit var foodAdapter: HomeAdapter
    private lateinit var searchView: SearchView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        setupSearchView(view)
        setupFoodList(view)

        return view
    }


    private fun setupSearchView(view: ConstraintLayout) {
        searchView = view.findViewById(R.id.searchView)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Food>()
            for (item in foodList) {
                if (item.name.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(item)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this.context, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                foodAdapter.setFilteredList(filteredList)
            }
        }
    }

    private fun setupFoodList(view: ConstraintLayout) {
        foodList = ArrayList()

        foodList.add(Food(R.drawable.food3, "Pizza Margarita", textLorem))
        foodList.add(Food(R.drawable.food2, "Pizza Concarni",textLorem))
        foodList.add(Food(R.drawable.food10, "Pizza Suprema",textLorem))
        foodList.add(Food(R.drawable.food4, "Shaorma Mare de Pui",textLorem))
        foodList.add(Food(R.drawable.food5, "Shaorma Mare de Vita",textLorem))
        foodList.add(Food(R.drawable.food6, "Burger",textLorem))
        foodList.add(Food(R.drawable.food7, "Pizza Diavola",textLorem))
        foodList.add(Food(R.drawable.food8, "Paste Carbonara",textLorem))
        foodList.add(Food(R.drawable.food9, "Paste Blognese",textLorem))
        foodList.add(Food(R.drawable.food1, "Pizza Quattro Stagioni",textLorem))

        setupRecycleViewAndFoodAdapter(view, foodList)
    }

    private fun setupRecycleViewAndFoodAdapter(view: ConstraintLayout, foodList: ArrayList<Food>) {

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        foodAdapter = HomeAdapter(foodList)
        recyclerView.adapter = foodAdapter

        foodAdapter.onItemClick = {

            val intent = Intent(requireContext(), DetailsFood::class.java)
            intent.putExtra("food", it)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}