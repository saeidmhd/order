package com.mahak.order.threeLevelAdapter;

import com.mahak.order.common.Category;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class Item extends RecyclerViewItem {

    String text = "";
    private int CategoryCode;
    private List<Category> categoryChildren = new ArrayList<>();


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Item(int level) {
        super(level);
    }

    public int getCategoryCode() {
        return CategoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        CategoryCode = categoryCode;
    }

    public List<Category> getCategoryChildren() {
        return categoryChildren;
    }

    public void setCategoryChildren(List<Category> categoryChildren) {
        this.categoryChildren.addAll(categoryChildren);
    }
}
