package com.iprody08.inquiryservice.telegram;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.restclient.ProductRestClient;
import com.iprody08.inquiryservice.config.BotConfig;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InquiryTelegramBot extends TelegramBot {

    private final Map<Long, TelegramInquiryDto> inquiryMap = new ConcurrentHashMap<>();

    private boolean startName = false;

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
        Long id = update.message().from().id();
        String message = update.message().text();
        if (startName) {
            enterName(id, message);
        } else if (message.equals("/start")) {
            log.info("Start telegram bot");
            ProductRestClient client = ProductRestClient
                    .builder()
                    .enableHttpsWithIgnoreSelfSignCertificate(true)
                    .url("https://localhost/products/all")
                    .build();
            List<ProductDto> listProduct = client.getListProducts().getBody();
            log.info(listProduct.toString());
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

            for (ProductDto p : listProduct) {
                markup.addRow(new InlineKeyboardButton(p.getSummary())
                        .callbackData("productId=" + p.getId()));
            }
            SendMessage request = new SendMessage(
                    id, "Hi, please, choose product: "
            ).replyMarkup(markup);
            this.execute(request);
            inquiryMap.put(id, new TelegramInquiryDto());
        }
    }

    private void callbackQueryHandler(Update update) {
        Long id = update.callbackQuery().from().id();
        String[] data = update.callbackQuery().data().trim().split("=");
        String key = data[0];
        String value = data[1];

        if (key.equals("productId")) {
            inquiryMap.get(id).setProductRefId(Long.parseLong(value));
            SendMessage request = new SendMessage(
                    id, """
                    Please, enter your first and last name according to the template: 'Firstname Lastname'
                    """
            );
            startName = true;
            this.execute(request);
        }

    }

    private void enterName(Long id, String name) {
        SendMessage request;
        String[] parseName = name.trim().split(" ");
        if (parseName.length < 2 || parseName[0].isEmpty() || parseName[1].isEmpty()) {
            log.info("Invalid input!");
            request = new SendMessage(
                    id, """
                    Invalid input!
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
                    """
            );
            startName = false;
        }
        this.execute(request);
    }


}
