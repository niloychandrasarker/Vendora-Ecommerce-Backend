package com.niloy.service;

import com.niloy.modal.Home;
import com.niloy.modal.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories); // Adjust the parameter type as needed
}
