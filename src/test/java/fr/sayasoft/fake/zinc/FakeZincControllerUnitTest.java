/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.sayasoft.fake.zinc;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.sayasoft.zinc.sdk.domain.OrderRequest;
import fr.sayasoft.zinc.sdk.domain.PaymentMethod;
import fr.sayasoft.zinc.sdk.domain.Product;
import fr.sayasoft.zinc.sdk.domain.RetailerCredentials;
import fr.sayasoft.zinc.sdk.domain.ZincAddress;
import fr.sayasoft.zinc.sdk.enums.ShippingMethod;
import fr.sayasoft.zinc.sdk.enums.ZincErrorCode;
import fr.sayasoft.zinc.sdk.enums.ZincWebhookType;
import org.hamcrest.core.StringContains;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.HashMap;

import static fr.sayasoft.fake.zinc.FakeZincController.POST_ORDER_RESPONSE;
import static fr.sayasoft.fake.zinc.FakeZincController.POST_ORDER_RESPONSE_TO_BE_REPLACED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FakeZincControllerUnitTest {
    private final OrderRequest orderRequest = OrderRequest.builder()
            .retailer("amazon")
            .products(Lists.newArrayList(new Product("0923568964")))
            .shippingAddress(
                    ZincAddress.builder()
                            .firstName("John Hannibal")
                            .lastName("Smith")
                            .addressLine1("1234 Main Street")
                            .addressLine2("above the bar")
                            .zipCode("11907")
                            .city("Brooklyn")
                            .state("NY")
                            .country("US")
                            .phoneNumber("123-123-1234")
                            .build()
            ).shippingMethod(ShippingMethod.cheapest) // TODO
            .billingAddress( // TODO parametrize
                    ZincAddress.builder()
                            .firstName("John Hannibal")
                            .lastName("Smith")
                            .addressLine1("1234 Main Street")
                            .addressLine2("above the bar")
                            .zipCode("11907")
                            .city("Brooklyn")
                            .state("NY")
                            .country("US")
                            .phoneNumber("123-123-1234")
                            .build()
            )
            .paymentMethod(
                    PaymentMethod.builder()
                            .nameOnCard("Hello World")
                            .number("0000000000000000")
                            .securityCode("000")
                            .expirationMonth(12)
                            .expirationYear(2020)
                            .useGift(false)
                            .build()
            )
            .retailerCredentials(new RetailerCredentials("test@test.fr", "password")) // TODO
            .maxPrice(0) // TODO
            .giftMessage("Here is your package") // TODO
            .isGift(true)
            .build();

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    public static final String POST_ORDER_REQUEST = "{\n" +
            "  \"client_token\": \"public\",\n" +
            "  \"idempotency_key\": \"XXX\", \n" +
            "  \"retailer\": \"amazon\",\n" +
            "  \"products\": [{\"product_id\": \"0923568964\", \"quantity\": 1}],\n" +
            "  \"max_price\": 2300,\n" +
            "  \"shipping_address\": {\n" +
            "    \"first_name\": \"Tim\",\n" +
            "    \"last_name\": \"Beaver\",\n" +
            "    \"address_line1\": \"77 Massachusetts Avenue\",\n" +
            "    \"address_line2\": \"\",\n" +
            "    \"zip_code\": \"02139\",\n" +
            "    \"city\": \"Cambridge\", \n" +
            "    \"state\": \"MA\",\n" +
            "    \"country\": \"US\",\n" +
            "    \"phone_number\": \"5551230101\"\n" +
            "  },\n" +
            "  \"is_gift\": true,\n" +
            "  \"gift_message\": \"Here's your package, Tim! Enjoy!\",\n" +
            "  \"shipping_method\": \"cheapest\",\n" +
            "  \"payment_method\": {\n" +
            "    \"name_on_card\": \"Ben Bitdiddle\",\n" +
            "    \"number\": \"5555555555554444\",\n" +
            "    \"security_code\": \"123\",\n" +
            "    \"expiration_month\": 1,\n" +
            "    \"expiration_year\": 2015,\n" +
            "    \"use_gift\": false\n" +
            "  },\n" +
            "  \"billing_address\": {\n" +
            "    \"first_name\": \"William\", \n" +
            "    \"last_name\": \"Rogers\",\n" +
            "    \"address_line1\": \"84 Massachusetts Ave\",\n" +
            "    \"address_line2\": \"\",\n" +
            "    \"zip_code\": \"02139\",\n" +
            "    \"city\": \"Cambridge\", \n" +
            "    \"state\": \"MA\",\n" +
            "    \"country\": \"US\",\n" +
            "    \"phone_number\": \"5551234567\"\n" +
            "  },\n" +
            "  \"retailer_credentials\": {\n" +
            "    \"email\": \"timbeaver@gmail.com\",\n" +
            "    \"password\": \"myAmazonPassword\"\n" +
            "  },\n" +
            "  \"webhooks\": {\n" +
//            "    \"order_placed\": \"http://mywebsite.com/zinc/order_placed\",\n" +
//            "    \"order_failed\": \"http://mywebsite.com/zinc/order_failed\",\n" +
//            "    \"tracking_obtained\": \"http://mywebsite.com/zinc/tracking_obtained\"\n" +
            "  },\n" +
            "  \"client_notes\": {\n" +
            "    \"our_internal_order_id\": \"abc123\"\n" +
            "  }\n" +
            "}";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

        this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {

        this.mockMvc.perform(get("/greeting").param("name", "Spring Community"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello, Spring Community!"));
    }

    @Test
    public void getOrder() throws Exception {

        this.mockMvc.perform(get("/v1/orders/1234546"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(FakeZincController.GET_ORDER_RESPONSE));
    }

    @Test
    public void postOrder_withString() throws Exception {
        this.mockMvc.perform(post("/v1/order")
                .contentType(contentType)
                .content(POST_ORDER_REQUEST))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(POST_ORDER_RESPONSE));
    }

    @Test
    public void postOrder_withObject() throws Exception {
        final String idempotencyKey = "Carina-Î²-Carinae-Miaplacidus";
        orderRequest.setIdempotencyKey(idempotencyKey);
        this.mockMvc.perform(post("/v1/order")
                .contentType(contentType)
                .content(new Gson().toJson(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(POST_ORDER_RESPONSE.replace(POST_ORDER_RESPONSE_TO_BE_REPLACED, idempotencyKey)));
    }

    @Test
    @Ignore("Run this test only when a webserver can receive and handle the request")
    public void postOrder_withWebHooks() throws Exception {
        final String idempotencyKey = "Carina-Î²-Carinae-Miaplacidus";
        orderRequest.setIdempotencyKey(idempotencyKey);
        orderRequest.setWebhooks(new HashMap<>(2));
        orderRequest.getWebhooks().put(ZincWebhookType.statusUpdated, "http://localhost:9090/webhook/statusUpdated/abcd");
        orderRequest.getWebhooks().put(ZincWebhookType.requestSucceeded, "http://localhost:9090/webhook/requestSucceeded/abcd");
        this.mockMvc.perform(
                post(
                        "/v1/order")
                        .contentType(contentType)
                        .content(new Gson().toJson(orderRequest))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(POST_ORDER_RESPONSE.replace(POST_ORDER_RESPONSE_TO_BE_REPLACED, idempotencyKey)));
    }

    @Test
    public void postOrder_KO() throws Exception {
        final String idempotencyKey = "Ursa-Major-Ursae-Majoris-Phecda";
        final String clientNotes = ZincErrorCode.invalid_quantity.name();
        orderRequest.setIdempotencyKey(idempotencyKey);
        orderRequest.setClientNotes(clientNotes);

        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),
                Charset.forName("utf8"));
        this.mockMvc.perform(post("/v1/order")
                .contentType(contentType)
                .content(new Gson().toJson(orderRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(StringContains.containsString("\"code\":\"invalid_quantity\"")))
                .andExpect(content().string(StringContains.containsString("\"message\":\"The quantity for one of the products does not match the one available on the retailer.\"")))
                .andExpect(content().string(StringContains.containsString("\"data\":\"{'fakeField': 'Ursa-Major-Ursae-Majoris-Phecda'}\"")))
                .andExpect(content().string(StringContains.containsString("\"_type\":\"error\"")))
        ;
    }

}
