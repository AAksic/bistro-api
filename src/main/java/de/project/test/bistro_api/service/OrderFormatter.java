package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;

public interface OrderFormatter<T> {

    T format(Order order);
}
