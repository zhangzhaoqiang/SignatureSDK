package com.tonghui.signaturelib;

/**
 * .----.
 * _.'__    `.
 * .--(Q)(OK)---/$\
 * .' @          /$$$\
 * :         ,   $$$$$
 * `-..__.-' _.-\$$/
 * `;_:    `"'
 * .'"""""`.
 * /,  ZZQ  ,\
 * //         \\
 * `-._______.-'
 * ___`. | .'___
 * (______|______)
 *
 * @包名: com.tonghui.signaturelib
 * @类名: MotionElement
 * @作者: 赵强
 * @创建时间: 2020/4/2 10:01
 * @描述: 触摸点信息
 */
public class MotionElement {

    public float x;
    public float y;
    /**
     * 压力值  物理设备决定的，和设计的设备有关系
     */
    public float pressure;
    /**
     * 绘制的工具是否是手指或者是笔（触摸笔）
     */
    public int toolType;

    public MotionElement(float mx, float my, float mp, int ttype) {
        x = mx;
        y = my;
        pressure = mp;
        toolType = ttype;
    }
}
