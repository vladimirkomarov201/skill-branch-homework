package ru.skillbranch.sbdelivery.core.notifier

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent

class BasketNotifierImpl : BasketNotifier {

    private val subject: ReplaySubject<BasketEvent> = ReplaySubject.create()

    override fun eventSubscribe(): Observable<BasketEvent> {
        return subject
    }

    override fun putDishes(dish: BasketEvent.AddDish) {
        subject.onNext(dish)
    }


}