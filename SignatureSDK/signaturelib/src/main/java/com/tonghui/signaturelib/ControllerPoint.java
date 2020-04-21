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
 * @类名: ControllerPoint
 * @作者: 赵强
 * @创建时间: 2020/4/2 10:00
 * @描述: 每个点的控制，关心三个因素：笔的宽度，坐标,透明数值
 */
public class ControllerPoint {

    public float x;
    public float y;

    public float width;
    public int alpha = 255;

    public ControllerPoint() {
    }

    public ControllerPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public void set(float x, float y, float w) {
        this.x = x;
        this.y = y;
        this.width = w;
    }


    public void set(ControllerPoint point) {
        this.x = point.x;
        this.y = point.y;
        this.width = point.width;
    }
}
