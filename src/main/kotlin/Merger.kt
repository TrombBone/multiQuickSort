class Merger(private val firstArr: IntArray, private val secondArr: IntArray) : Thread() {

    private val resultArr: IntArray = IntArray(firstArr.size + secondArr.size)

    override fun run() {
        merge()
    }

    private fun merge() {
        var firstIndex = 0 // Указатель на элемент в первом подмассиве
        var secondIndex = 0 // Указатель на элемент в правом подмассиве
        var resultIndex = 0 // Указатель на место в результирующем массиве

        // Выполняем данные проверки, пока не пройдём по одному из подмассивов
        while (firstIndex < firstArr.size && secondIndex < secondArr.size) {
            // Сравниваем элементы из двух подмассивов и помещаем в результирующий массив меньший из них
            if (firstArr[firstIndex] <= secondArr[secondIndex]) resultArr[resultIndex++] = firstArr[firstIndex++]
            else resultArr[resultIndex++] = secondArr[secondIndex++]
        }

        // Проверяем остались ли какие-либо элементы в оставшемся подмассиве
        // Если да, то переносим их в результирующий
        if (firstIndex == firstArr.size)
            while (secondIndex < secondArr.size) resultArr[resultIndex++] = secondArr[secondIndex++]
        else if (secondIndex == secondArr.size)
            while (firstIndex < firstArr.size) resultArr[resultIndex++] = firstArr[firstIndex++]
    }

    fun getResultArr() = resultArr
}