package com.example.registerapplication.Adapters;

public class ViewUtils {
    /**
     * 根据图片宽高比计算显示高度
     * @param imageWidth 图片原始宽度（像素）
     * @param imageHeight 图片原始高度（像素）
     * @param columnWidth 列宽（像素）
     * @return 计算后的显示高度
     */
    public static int calculateImageHeight(int imageWidth, int imageHeight, int columnWidth) {
        if (imageWidth <= 0 || imageHeight <= 0) {
            return columnWidth; // 默认正方形比例
        }
        return (int) (columnWidth * ((float) imageHeight / imageWidth));
    }
}