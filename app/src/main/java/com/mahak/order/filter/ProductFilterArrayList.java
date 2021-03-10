package com.mahak.order.filter;

import android.widget.Filter;

import com.mahak.order.common.Customer;
import com.mahak.order.common.Product;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.fragment.RecyclerProductAdapter;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProductFilterArrayList extends Filter {
    ArrayList<Product> arrayOrginal;
    DbAdapter dbAdapter;
    public ProductFilterArrayList(ArrayList<Product> productArrayList, DbAdapter db){
        arrayOrginal = productArrayList;
        dbAdapter = db;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        constraint = constraint.toString().toLowerCase();
        FilterResults result = new FilterResults();
        Boolean CheckFilter = false;
        String name;
        String code;
        boolean result_contain = false;
        boolean result_contain_reverse = false;

        Set<Product> set = new LinkedHashSet<>();
        if (constraint.toString().length() > 0) {
            for (int i = 0; i < arrayOrginal.size(); i++) {
                Product product = arrayOrginal.get(i);
                name = product.getName().toLowerCase();
                String[] searchArray = constraint.toString().split(" ");
                String[] nameArray = name.split(" ");
                for (String search : searchArray)
                    for (String name1 : nameArray) {
                        result_contain = ServiceTools.CheckContainsWithSimillar(search, name1);
                        if (result_contain)
                            break;
                    }
                Collections.reverse(Arrays.asList(searchArray));
                for (String search : searchArray)
                    for (String name1 : nameArray) {
                        result_contain_reverse = ServiceTools.CheckContainsWithSimillar(search, name1);
                        if (result_contain_reverse)
                            break;
                    }
                if (result_contain && result_contain_reverse) {
                    set.add(product);
                    CheckFilter = true;
                }
            }

            for (int i = 0; i < arrayOrginal.size(); i++) {
                Product product = arrayOrginal.get(i);
                code = String.valueOf(product.getProductCode());
                result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), code.toLowerCase());
                if (result_contain) {
                    set.add(product);
                }
            }

            ArrayList<Product> filterItem = new ArrayList<>(set);
            Collections.sort(filterItem, new ProductSortingComparator());

            result.values = filterItem;
            result.count = filterItem.size();
        } else {
            synchronized (this) {
                result.values = arrayOrginal;
                result.count = arrayOrginal.size();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        RecyclerProductAdapter.products = (ArrayList<Product>) results.values;

    }
}
