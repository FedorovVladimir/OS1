import kotlin.random.Random

/**
 * Процессор простаивает меньше при случае, когда задач cpu/io 80/20 больше
 * Наилучшая скорость выполнения при случае, когда задач промерно одинаковое количество
 */

object Main {

    data class Task(var number: Int, var cpu: Int, var io: Int)

    @JvmStatic
    fun main(args: Array<String>) {
        val lists: ArrayList<ArrayList<Task>> = arrayListOf()
        lists.add(arrayListOf())
        lists.add(arrayListOf())
        lists.add(arrayListOf())
        for (number in 1..950) {
            val r = Random.nextInt(10)
            lists[0].add(Task(1000 + number, 20 + r, 80 - r))
        }
        for (number in 1..950) {
            val r = Random.nextInt(10)
            lists[1].add(Task(2000 + number, 45 + r, 55 - r))
        }
        for (number in 1..1100) {
            val r = Random.nextInt(10)
            lists[2].add(Task(3000 + number, 70 + r, 30 - r))
        }
        for (list in lists) {
            println(list)
        }

        var stopsCpu = 0
        var stopsIo = 0

        val delta = 10
        var deltaCpu = delta
        var deltaIo = delta

        var forwardCpuList = 0
        var forwardIoList = 0

        var currentCpuTask: Task? = null
        var currentCpuList: ArrayList<Task>? = null

        var currentIoTask: Task? = null
        var currentIoList: ArrayList<Task>? = null

        while (true) {
            // select tasks
            var i = 0
            while (i < lists.size && currentCpuTask == null) {
                for (task in lists[forwardCpuList]) {
                    if (task.cpu > 0 && task.io == 0) {
                        currentCpuList = lists[forwardCpuList]
                        currentCpuTask = task
                        deltaCpu = delta
                    }
                }
                forwardCpuList = (forwardCpuList + 1) % lists.size
                i++
            }

            var j = 0
            while (j < lists.size && currentIoTask == null) {
                for (task in lists[forwardIoList]) {
                    if (task.io > 0) {
                        currentIoList = lists[forwardIoList]
                        currentIoTask = task
                        deltaIo = delta
                    }
                }
                forwardIoList = (forwardIoList + 1) % lists.size
                j++
            }
            if (currentCpuTask == null && currentIoTask == null) {
                break
            }

            // work cpu
            if (currentCpuTask != null) {
                currentCpuTask.let {
                    it.cpu--
                    deltaCpu--
                }
                print("CPU l$forwardCpuList: $currentCpuTask ")

                if (currentCpuTask.cpu == 0) {
                    if (currentCpuTask.io == 0) {
                        currentCpuList?.remove(currentCpuTask)
                    }
                    currentCpuList = null
                    currentCpuTask = null
                }

                if (deltaCpu == 0) {
                    currentCpuList = null
                    currentCpuTask = null
                }
            } else {
                stopsCpu++
                print("CPU: stop ")
            }

            // work io
            if (currentIoTask != null) {
                currentIoTask.let {
                    it.io--
                    deltaIo--
                }
                print("IO l$forwardIoList: $currentIoTask ")

                if (currentIoTask.io == 0) {
                    if (currentIoTask.cpu == 0) {
                        currentIoList?.remove(currentIoTask)
                    }
                    currentIoList = null
                    currentIoTask = null
                }

                if (deltaIo == 0) {
                    currentIoList = null
                    currentIoTask = null
                }
            } else {
                stopsIo++
                print("IO: stop ")
            }

            println()
//            sleep(400)
        }
        println("the end")
        println("CPU stops: $stopsCpu")
        println("IO stops: $stopsIo")
        println("ALL stops: " + (stopsCpu + stopsIo))
    }
}
