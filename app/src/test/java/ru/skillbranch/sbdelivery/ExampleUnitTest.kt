package ru.skillbranch.sbdelivery

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.IllegalStateException
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resumeWithException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        var localScope: CoroutineScope  = CoroutineScope(SupervisorJob() )
        val sJob = SupervisorJob().apply {
            invokeOnCompletion {
                println("complete")
            }
        }

        val handler = CoroutineExceptionHandler{ _, tr ->
            println("catch in handler" +tr )
        }
        val scope = CoroutineScope (Job ())
//        runBlocking {
//            try {
//                coroutineScope {


        runBlocking {
            scope.launch (){
                launch(handler) {
                    throw IllegalStateException()
                }
        }



                    }
//                }
            /*}catch (e:Throwable){
               println("catch try")
            }*/



//        }

    }
}