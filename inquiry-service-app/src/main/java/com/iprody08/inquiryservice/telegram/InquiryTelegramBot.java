package com.iprody08.inquiryservice.telegram;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.restclient.ProductRestClient;
import com.iprody08.customerservice.dto.CustomerDto;
import com.iprody08.customerservice.dto.restclient.CustomerRestClient;
import com.iprody08.inquiryservice.config.BotConfig;
import com.iprody08.inquiryservice.dto.InquiryDto;
import com.iprody08.inquiryservice.dto.SourceDto;
import com.iprody08.inquiryservice.telegram.dto.TelegramInquiryDto;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class InquiryTelegramBot extends TelegramBot {

    private final Map<Long, TelegramInquiryDto> inquiryMap = new ConcurrentHashMap<>();

    private boolean startName = false;

    private boolean startEmail = false;

    @Autowired
    public InquiryTelegramBot(BotConfig botConfig) {
        super(botConfig.getBotToken());
    }

    @PostConstruct
    public void run() {
        this.setUpdatesListener(updateList -> {
            for (Update update : updateList) {
                if (update.message() != null) {
                    messageHandler(update);
                } else {
                    callbackQueryHandler(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void messageHandler(Update update) {
        String telegramId = update.message().from().username();
        log.info("User {} send message", telegramId);
        Long id = update.message().from().id();
        String message = update.message().text();
        if (startName) {
            enterName(id, message);
        } else if (startEmail) {
            enterEmail(id, message);
        } else {
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.addRow(new InlineKeyboardButton("courses")
                    .callbackData("listProducts=true"));

            SendMessage request = new SendMessage(
                    id, "Courses"
            ).replyMarkup(markup);
            this.execute(request);
        }
    }

    private void callbackQueryHandler(Update update) {
        String telegramId = "@" + update.callbackQuery().from().username();
        Long id = update.callbackQuery().from().id();
        String[] data = update.callbackQuery().data().trim().split("=");
        String key = data[0];
        String value = data[1];

        switch (key) {
            case "productId" -> {
                SendMessage request;
                inquiryMap.get(id).setProductRefId(Long.parseLong(value));
                try {
                    CustomerRestClient client = CustomerRestClient
                            .builder()
                            .enableHttpsWithIgnoreSelfSignCertificate(true)
                            .url("https://localhost/api/customers/telegram/" + telegramId)
                            .build();
                    CustomerDto customerDto = client.getCustomer().getBody();

                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    markup.addRow(new InlineKeyboardButton("right")
                            .callbackData("checkCustomer=right"));
                    markup.addRow(new InlineKeyboardButton("wrong")
                            .callbackData("checkCustomer=wrong"));
                    request = new SendMessage(
                            id, String.format("""
                                    Please, check your user data:
                                    Firstname: %s
                                    LastName: %s
                                    Country: %s
                                    Email: %s
                                    Telegram id: %s
                                    """,
                            customerDto.getName(),
                            customerDto.getSurname(),
                            customerDto.getCountryName(),
                            customerDto.getEmail(),
                            customerDto.getTelegramId())
                    ).replyMarkup(markup);
                } catch (Exception e) {
                    request = new SendMessage(
                            id, """
                            Please, enter your first and last name according to the template: 'Firstname Lastname'
                            """
                    );
                    startName = true;
                }
                this.execute(request);
            }
            case "checkCustomer" -> {
                SendMessage request;
                if (value.equals("right")) {
                    request = new SendMessage(
                            id, """
                            Your inquiry has been registered! The manager will contact you shortly.
                            """
                    );
                    //TODO Need to do Inquiry
                } else {
                    request = new SendMessage(
                            id, """
                            Please, enter your first and last name according to the template: 'Firstname Lastname'
                            """
                    );
                    startName = true;
                }
                this.execute(request);
            }
            case "listProducts" -> {
                ProductRestClient client = ProductRestClient
                        .builder()
                        .enableHttpsWithIgnoreSelfSignCertificate(true)
                        .url("https://localhost/api/products/all")
                        .build();
                List<ProductDto> listProduct = client.getListProducts().getBody();
                log.info("User get list products");
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

                for (ProductDto p : listProduct) {
                    markup.addRow(new InlineKeyboardButton(p.getSummary())
                            .callbackData("productId=" + p.getId()));
                }
                SendMessage request = new SendMessage(
                        id, "Please, choose product: "
                ).replyMarkup(markup);
                this.execute(request);
                inquiryMap.put(id, new TelegramInquiryDto());
            }
            case "Russian Federation" -> {
                Long countryId = Long.parseLong(value);
                enterCountry(id, telegramId, key, countryId);
            }
            default -> {

            }
        }

    }

    private void enterCountry(Long id, String telegramId, String countryName, Long countryId) {
        TelegramInquiryDto dto = inquiryMap.get(id);
        CustomerDto addCustomer = new CustomerDto(
                null,
                dto.getFirstName(),
                dto.getLastName(),
                countryId,
                countryName,
                dto.getEmail(),
                telegramId,
                null,
                null,
                null,
                null
        );
        CustomerRestClient client = CustomerRestClient
                .builder()
                .enableHttpsWithIgnoreSelfSignCertificate(true)
                .url("https://localhost/api/customers/")
                .build();
        RestTemplate customerRestTemplate = client.getRestTemplate();
        log.info("Add new customer");
        CustomerDto added = customerRestTemplate.postForObject(
                "https://localhost/api/customers/",
                addCustomer,
                CustomerDto.class
        );

        InquiryDto inquiryDto = new InquiryDto(
                null,
                new SourceDto(null, "telegram"),
                "",
                "",
                "",
                dto.getProductRefId(),
                dto.getCustomerRefId(),
                dto.getManagerRefId()
        );


    }

    private void enterName(Long id, String name) {
        SendMessage request;
        String[] parseName = name.trim().split(" ");
        if (parseName.length < 2 || parseName[0].isEmpty() || parseName[1].isEmpty()) {
            log.info("Invalid input!");
            request = new SendMessage(
                    id, """
                    Invalid input name!
                    Please, enter your first and last name according to the template: 'Firstname Lastname'
                    """
            );
        } else  {
            TelegramInquiryDto dto = inquiryMap.get(id);
            dto.setFirstName(parseName[0]);
            dto.setLastName(parseName[1]);

            log.info("Successful input!");
            request = new SendMessage(
                    id, """
                    Your name successfully save!
                    ============================
                    Input your email:
                    """
            );
            startName = false;
            startEmail = true;
        }
        this.execute(request);
    }

    private void enterEmail(Long id, String email) {
        SendMessage request;
        Pattern pattern = Pattern.compile("([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.addRow(new InlineKeyboardButton("Russian Federation")
                    .callbackData("Russian Federation=183"));
            markup.addRow(new InlineKeyboardButton("Latvia")
                    .callbackData("Latvia=123"));
            markup.addRow(new InlineKeyboardButton("India British")
                    .callbackData("IOT=33"));

            request = new SendMessage(
                    id, """
                    Success!
                    ==================
                    Choose your country:
                    """
            ).replyMarkup(markup);
            TelegramInquiryDto dto = inquiryMap.get(id);
            dto.setEmail(email);
            startEmail = false;

        } else {
            log.info("Invalid input email!");
            request = new SendMessage(
                    id, """
                    Invalid email!
                    """
            );
        }
        this.execute(request);
    }


}
