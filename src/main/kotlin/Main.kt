import kotlin.random.Random

/**
 * Процессор простаивает меньше при случае, когда задач cpu/io 80/20 больше
 * Наилучшая скорость выполнения при случае, когда задач промерно одинаковое количество
 */

object Main {

    data class Task(var isCpu: Boolean, var n: Int, var cpu: Int, var io: Int)

    @JvmStatic
    fun main(args: Array<String>) {
        val list: ArrayList<Task> = arrayListOf()
        for (number in 1..2) {
            val r = Random.nextInt(10)
            list.add(Task(Random.nextBoolean(), 1000 + number, 20 + r, 80 - r))
        }
        for (number in 1..2) {
            val r = Random.nextInt(10)
            list.add(Task(Random.nextBoolean(), 2000 + number, 45 + r, 55 - r))
        }
        for (number in 1..2) {
            val r = Random.nextInt(10)
            list.add(Task(Random.nextBoolean(), 3000 + number, 70 + r, 30 - r))
        }
        println(list)

        var selectCpu = true
        var selectIo = true
        var indexCpu = -1
        var indexIo = -1
        var deltaCpu = 0
        var deltaIo = 0
        while (true) {
            if (selectCpu) {
                for (i in 0..list.size) {
                    indexCpu = (indexCpu + 1) % list.size
                    if (list[indexCpu].isCpu) {
                        selectCpu = !selectCpu
                        deltaCpu = Random.nextInt(10)
                        break
                    }
                }
            } else {
                if (deltaCpu > 0 && list[indexCpu].cpu > 0) {
                    deltaCpu--
                    list[indexCpu].cpu--
                    if (list[indexCpu].cpu == 0 && list[indexCpu].io == 0) {
                        list.removeAt(indexCpu)
                        selectCpu = !selectCpu
                        if (indexIo > indexCpu) {
                            indexIo--
                        }
                        if (indexCpu > 0) {
                            indexCpu--
                        }
                    }
                } else {
                    list[indexCpu].isCpu = !list[indexCpu].isCpu
                    selectCpu = !selectCpu
                }
            }

            if (selectIo) {
                for (i in 0..list.size) {
                    indexIo = (indexIo + 1) % list.size
                    if (!list[indexIo].isCpu) {
                        selectIo = !selectIo
                        deltaIo = Random.nextInt(10)
                        break
                    }
                }
            } else {
                if (deltaIo > 0 && list[indexIo].io > 0) {
                    deltaIo--
                    list[indexIo].io--
                    if (list[indexIo].cpu == 0 && list[indexIo].io == 0) {
                        list.removeAt(indexIo)
                        selectIo = !selectIo
                        if (indexCpu > indexIo) {
                            indexCpu--
                        }
                        if (indexIo > 0) {
                            indexIo--
                        }
                    }
                } else {
                    list[indexIo].isCpu = !list[indexIo].isCpu
                    selectIo = !selectIo
                }
            }


            if (list.isEmpty()) {
                break
            } else {
                println("work cpu: ${list[indexCpu].n}($indexCpu) work io: ${list[indexIo].n}($indexIo)")
                println(list)
            }
//            readLine()
        }
    }
}
