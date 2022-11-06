class Sorter(val sortableArr: IntArray) : Thread() {
    override fun run() {
        sort(sortableArr, 0, sortableArr.size)
    }

    private fun sort(arr: IntArray, leftIndex: Int, rightIndex: Int) {
        if (leftIndex >= rightIndex - 1) return // Если в массиве только один элемент, значит что он уже отсортирован

        val pivotalIndex = partition(arr, leftIndex, rightIndex)
        sort(arr, leftIndex, pivotalIndex) // Проводим сортировку для элементов слева от последнего опорного элемента
        sort(arr, pivotalIndex + 1, rightIndex) // Аналогично справа
    }

    /*
     * Сортируем массив так, чтобы получить две части:
     * слева от опорного элемента: элементы меньшие или равные опорному;
     * справа от опорного элемента: элементы большие опорного.
     * Из метода возвращаем индекс местонахождения опорного элемента после перестановок.
     */
    private fun partition(arr: IntArray, leftIndex: Int, rightIndex: Int): Int {
        // В качестве опорного элемента выбираем первый элемент массива
        val pivotal: Int = arr[leftIndex]
        var i = leftIndex
        for (j in leftIndex + 1 until rightIndex) if (arr[j] <= pivotal) swap(arr, ++i, j)
        swap(arr, i, leftIndex)
        return i
    }

    private fun swap(arr: IntArray, left: Int, right: Int) {
        val tmp = arr[left]
        arr[left] = arr[right]
        arr[right] = tmp
    }

//    @JvmName("getSortableArr1")
//    fun getSortableArr() = sortableArr
}
