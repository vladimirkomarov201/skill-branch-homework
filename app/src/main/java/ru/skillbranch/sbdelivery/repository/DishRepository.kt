package ru.skillbranch.sbdelivery.repository

import ru.skillbranch.sbdelivery.data.db.dao.CartDao
import ru.skillbranch.sbdelivery.data.db.dao.DishesDao
import ru.skillbranch.sbdelivery.data.network.RestService
import ru.skillbranch.sbdelivery.data.network.res.ReviewRes
import ru.skillbranch.sbdelivery.data.toDishContent
import ru.skillbranch.sbdelivery.screens.dish.data.DishContent
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface IDishRepository {
    suspend fun findDish(id: String): DishContent
    suspend fun addToCart(id: String, count: Int)
    suspend fun cartCount(): Int
    suspend fun loadReviews(dishId: String): List<ReviewRes>
    suspend fun sendReview(id: String, rating: Int, review: String): ReviewRes
}

class DishRepository @Inject constructor(
    private val api: RestService,
    private val dishesDao: DishesDao,
    private val cartDao: CartDao,
) : IDishRepository {
    override suspend fun findDish(id: String): DishContent = dishesDao.findDish(id).toDishContent()

    override suspend fun addToCart(id: String, count: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun cartCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun loadReviews(dishId: String): List<ReviewRes> {
        return api.getReviews(dishId).map {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            ReviewRes(
                name = it.author,
                date = dateFormatter.parse(it.date).time,
                rating = it.rating,
                message = it.text
            )
        }
    }

    override suspend fun sendReview(id: String, rating: Int, review: String): ReviewRes {
        TODO("Not yet implemented")
    }
}