package com.example.registerapplication.Entity.Data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 滑动列表实体类
 *
 */
@NoArgsConstructor
@Data

public class TitleInfo {
    private String title;
    private String pu_title;

    public TitleInfo(String title, String pu_title) {
        this.title = title;
        this.pu_title = pu_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPu_title() {
        return pu_title;
    }

    public void setPu_title(String pu_title) {
        this.pu_title = pu_title;
    }
}