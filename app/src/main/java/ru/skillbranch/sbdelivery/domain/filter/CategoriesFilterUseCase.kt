package ru.skillbranch.sbdelivery.domain.filter

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError

class CategoriesFilterUseCase(private val repository: DishesRepositoryContract) : CategoriesFilter {
    override fun categoryFilterDishes(categoryId: String): Single<List<DishEntity>> {
        return repository.getCachedDishes()
            .flatMapObservable{
                Observable.fromIterable(it)
            }
            .filter {
                it.categoryId == categoryId
            }
            .toList()
            .flatMap {
                if (it.isEmpty())
                    Single.error(EmptyDishesError("empty"))
                else
                    Single.just(it)
            }
            .subscribeOn(Schedulers.io())

    }
}