import kotlin.random.Random

fun main() {
    val testingSize = 100
    val testingArray = IntArray(testingSize) { Random.nextInt(-100, 100) }
    multiThreadSort(testingArray, true)
}


