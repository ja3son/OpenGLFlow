package com.ja3son.gllib.demo.skeletal_anim.basic

internal class DoActionThread(var robot: Robot) : Thread() {
    //当前动作编号
    var currActionIndex = 0
    //当前的动作编号细节编号
    var currStep = 0
    //当前动作数据
    var currAction: Action? = null

    override fun run() {
        try {
            sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        //拿到当前的动作编号
        currAction = ActionGenerator.acArray[currActionIndex]

        while (true) {
            //变成最原始的初始变化矩阵
            robot.backToInit()
            //如果此次动画播放完了，则进行下一组动画的播放
            if (currStep >= currAction!!.totalStep) {
                //取余控制动作的编号在一定范围内
                currActionIndex = (currActionIndex + 1) % ActionGenerator.acArray.size
                //重新获取当前的动作编号
                currAction = ActionGenerator.acArray[currActionIndex]
                currStep = 0 //当前的动作编号细节编号，变为0;
            }
            //将ActionGenerator中给出的动作数据进行分解
            //修改数据
            for (ad in currAction!!.data) {
                //部件索引
                val partIndex = ad[0].toInt()
                //动作类型
                val aType = ad[1].toInt()
                //若aType==0此部件动作为平移
                if (aType == 0) {
                    val xStart = ad[2] //起始位置的x坐标
                    val yStart = ad[3] //起始位置的y坐标
                    val zStart = ad[4] //起始位置的z坐标
                    val xEnd = ad[5] //部件最终要到达位置的x坐标
                    val yEnd = ad[6] //部件最终要到达位置的y坐标
                    val zEnd = ad[7] //部件最终要到达位置的z坐标
                    //根据当前的动作编号细节编号，计算出当前的平移距离
                    val currX = xStart + (xEnd - xStart) * currStep / currAction!!.totalStep
                    val currY = yStart + (yEnd - yStart) * currStep / currAction!!.totalStep
                    val currZ = zStart + (zEnd - zStart) * currStep / currAction!!.totalStep
                    //将当前部件的平移信息记录进变换矩阵
                    robot.bpArray[partIndex].transtate(currX, currY, currZ)
                } else if (aType == 1) { //若aType==0此部件动作为旋转
                    val startAngle = ad[2] //旋转的起始角度
                    val endAngle = ad[3] //旋转的介绍角度
                    //根据当前的动作编号细节编号出当前的旋转角度
                    val currAngle = startAngle + (endAngle - startAngle) * currStep / currAction!!.totalStep
                    //此部件的旋转轴
                    val x = ad[4]
                    val y = ad[5]
                    val z = ad[6]
                    //将当前部件的旋转信息记录进变换矩阵
                    robot.bpArray[partIndex].rotate(currAngle, x, y, z)
                }
            }
            robot.updateState() //层次级联更新骨骼矩阵的方法
            //将最终矩阵内容拷贝进绘制用的最终矩阵
            robot.flushDrawData()
            currStep++ //当前的动作编号细节编号+1
            try {
                sleep(30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}