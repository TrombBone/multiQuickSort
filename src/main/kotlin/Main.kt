import java.util.*
import kotlin.random.Random

fun main() {
    val testingSizes = intArrayOf(1_000, 100_000, 1_000_000, 10_000_000, 100_000_000)
    for (arrSize in testingSizes) {
        // Заполнение массива заданной длины случайными значениями
        val testingArray = IntArray(arrSize) { Random.nextInt() }

        multiThreadSort(testingArray)
        oneThreadSort(testingArray)
    }
}

/**
 * Multithreading sort
 */
fun multiThreadSort(testingArray: IntArray, isPrint: Boolean = false) {
    val arrSize = testingArray.size
    println("\nArrays size for multithreading sort: $arrSize")

    var resultArray = IntArray(testingArray.size)

    if (isPrint) {
        println("Array before sort:")
        printlnIntArray(testingArray)
    }

    var startTime: Long
    var elapsedTime: Long

    var threadPoolSize = 2
    while (threadPoolSize <= 16) {

        // Начало отсчёта времени
        startTime = System.currentTimeMillis()

        // Этап "Разделения"
        val subSize = arrSize / threadPoolSize
        var subArrays = Array(threadPoolSize - 1) { IntArray(subSize) }
        var lastSubArr = IntArray(arrSize - (threadPoolSize - 1) * subSize)
        val threads = arrayOfNulls<Sorter>(threadPoolSize)

        // Этап "Сортировки"
        for (subArrIndex in 0 until threadPoolSize - 1) {
            System.arraycopy(testingArray, subSize * subArrIndex, subArrays[subArrIndex], 0, subSize)
            threads[subArrIndex] = Sorter(subArrays[subArrIndex])
            threads[subArrIndex]!!.start()
        }
        System.arraycopy(
            testingArray, subSize * (threadPoolSize - 1), lastSubArr,
            0, arrSize - (threadPoolSize - 1) * subSize
        )
        threads[threadPoolSize - 1] = Sorter(lastSubArr)
        threads[threadPoolSize - 1]!!.start()
        for (i in 0 until threadPoolSize) threads[i]!!.join()
        for (i in 0 until threadPoolSize - 1) System.arraycopy(threads[i]!!.sortableArr, 0, subArrays[i], 0, subSize)
        System.arraycopy(
            threads[threadPoolSize - 1]!!.sortableArr, 0, lastSubArr,
            0, arrSize - (threadPoolSize - 1) * subSize
        )

        // Этап "Слияния"
        var lastSubSize = subSize
        var numberOfArraysForLastStage = threadPoolSize
        while (numberOfArraysForLastStage > 1) {
            val numberOfArraysForCurrentStage = numberOfArraysForLastStage / 2
            val mergers = arrayOfNulls<Merger>(numberOfArraysForLastStage / 2)
            for (i in 0 until numberOfArraysForCurrentStage - 1) {
                mergers[i] = Merger(subArrays[2 * i], subArrays[2 * i + 1])
                mergers[i]!!.start()
            }
            mergers[numberOfArraysForCurrentStage - 1] = Merger(
                subArrays[(numberOfArraysForCurrentStage - 1) * 2],
                lastSubArr
            )
            mergers[numberOfArraysForCurrentStage - 1]!!.start()
            for (i in 0 until numberOfArraysForCurrentStage) mergers[i]!!.join()
            val currentSubSize = 2 * lastSubSize
            subArrays = Arrays.copyOf(subArrays, numberOfArraysForCurrentStage)
            for (i in 0 until numberOfArraysForCurrentStage - 1) {
                subArrays[i] = subArrays[i].copyOf(currentSubSize)
            }
            lastSubArr = lastSubArr.copyOf(arrSize - currentSubSize * (numberOfArraysForCurrentStage - 1))
            for (i in 0 until numberOfArraysForCurrentStage - 1)
                System.arraycopy(mergers[i]!!.getResultArr(), 0, subArrays[i], 0, currentSubSize)

            System.arraycopy(
                mergers[numberOfArraysForCurrentStage - 1]!!.getResultArr(), 0, lastSubArr,
                0, arrSize - currentSubSize * (numberOfArraysForCurrentStage - 1)
            )
            lastSubSize = currentSubSize
            numberOfArraysForLastStage = numberOfArraysForCurrentStage
        }

        // Конец подсчёта времени
        elapsedTime = System.currentTimeMillis() - startTime
        println("Sorting time for $arrSize elements with $threadPoolSize threads is ${elapsedTime.toDouble() / 1000}")

        threadPoolSize *= 2

        resultArray = lastSubArr
    }

    if (isPrint) {
        println("Array after sort:")
        printlnIntArray(resultArray)
    }
}

/**
 * One-thread sort
 */
fun oneThreadSort(testingArray: IntArray, isPrint: Boolean = false) {
    val arrSize = testingArray.size
    println("\nArrays size for one-thread sort: $arrSize")

    if (isPrint) {
        println("Array before sort:")
        printlnIntArray(testingArray)
    }

    // Начало отсчёта времени
    val startTime: Long = System.currentTimeMillis()

    val oneThreadSort = Sorter(testingArray)
    oneThreadSort.start()
    oneThreadSort.join()

    // Конец отсчёта времени
    val elapsedTime: Long = System.currentTimeMillis() - startTime

    println("Sorting time for $arrSize elements with 1 thread is ${elapsedTime.toDouble() / 1000}")

    if (isPrint) {
        println("Array after sort:")
        printlnIntArray(testingArray)
    }
}

fun printlnIntArray(ints: IntArray) {
    print("[")
    for (i in 0 until ints.size - 1) print("${ints[i]}, ")
    print("${ints[ints.size - 1]}]\n")
}
