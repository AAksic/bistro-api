package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class OrderToReceiptFormatter implements OrderFormatter<String> {

    private static final String RECEIPT_TEMPLATE = """
            -------------------------<br/>
            Table Nr. [[${orderId}]]<br/>
            -------------------------<br/>
            <th:block th:each="item : ${orderItems}">
            [[${item.quantity}]] x [[${item.name}]] @ [[${item.price}]] = [[${itemsTotal}]]<br/>
            </th:block>
            -------------------------<br/>
            Subtotal: [[${subtotal}]]<br/>
            Discount: [[${discount}]]%
            Total: [[${total}]]
            """;

    @Override
    public String format(Order order) {
        StringTemplateResolver receiptTemplateResolver = new StringTemplateResolver();
        receiptTemplateResolver.setTemplateMode(TemplateMode.TEXT);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(receiptTemplateResolver);

        Context context = new Context();
        context.setVariable("orderId", order.getId());
        context.setVariable("orderItems", order.getItems());

        return templateEngine.process(RECEIPT_TEMPLATE, context);
    }
}
