package com.rashad.currency.service.impl;

import com.rashad.currency.entity.Currency;
import com.rashad.currency.repository.CurrencyRepository;
import com.rashad.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final RestTemplate restTemplate;
    private final CurrencyRepository currencyRepository;

    @Override
    public String fetchCurrencyFromCBAR(String date)
            throws ParserConfigurationException, IOException, SAXException {

        if (!checkDateFormat(date)) {
            throw new IllegalStateException("date: Tarix düzgün formatda daxil edilməyib. " +
                    "Düzgün format bu şəkildə olmalıdır: dd.MM.yyyy");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateTime = LocalDate.parse(date, formatter);
        if (!currencyRepository.existsByDate(dateTime)) {
            String url = "https://www.cbar.az/currencies/" + date + ".xml";
            restTemplate.getMessageConverters().add(
                    0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String body = restTemplate.getForEntity(url, String.class).getBody();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(body)));
            NodeList valute = doc.getElementsByTagName("Valute");

            saveCurrencyInDB(valute, date);
            return date + " tarixi üçün olan valyuta məzənnəsi bazaya əlavə olundu.";
        }
        return date + " tarixi üçün valyuta məzənnəsi artıq bazada mövcuddur!";
    }

    private void saveCurrencyInDB(NodeList valute, String date) {
        for (int i = 0; i < valute.getLength(); i++) {
            Currency currency = new Currency();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateTime = LocalDate.parse(date, formatter);
            currency.setDate(dateTime);
            currency.setType(valute.item(i).getParentNode().getAttributes().item(0).getNodeValue());
            currency.setCode(valute.item(i).getAttributes().item(0).getNodeValue());

            NodeList data = valute.item(i).getChildNodes();
            for (int j = 0; j < data.getLength(); j++) {
                Node metalData = data.item(j);
                if (!metalData.getNodeName().equals("#text")) {
                    if (metalData.getNodeName().equals("Nominal"))
                        currency.setNominal(metalData.getTextContent());
                    if (metalData.getNodeName().equals("Name"))
                        currency.setName(metalData.getTextContent());
                    if (metalData.getNodeName().equals("Value"))
                        currency.setValue(metalData.getTextContent());
                }
            }
            currencyRepository.save(currency);
        }
    }

    @Override
    public String deleteFromDB(String date) {
        if (!checkDateFormat(date)) {
            throw new IllegalStateException("date: Tarix düzgün formatda daxil edilməyib. " +
                    "Düzgün format bu şəkildə olmalıdır: dd.MM.yyyy");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateTime = LocalDate.parse(date, formatter);
        List<Currency> currency = currencyRepository.findByDate(dateTime);
        if (currency.isEmpty())
            return date + " tarixi üçüm məzənnələr bazada mövcud deyil";
        currencyRepository.deleteAll(currency);
        return date + " tarixi üçüm məzənnələr bazadan silindi";
    }

    @Override
    public List<Currency> getAllCurrencyByCode(String code) {
        return currencyRepository.findByCodeOrderByDate(code);
    }

    @Override
    public List<Currency> getAllCurrencyByDate(String date) {
        if (!checkDateFormat(date)) {
            throw new IllegalStateException("date: Tarix düzgün formatda daxil edilməyib. " +
                    "Düzgün format bu şəkildə olmalıdır: dd.MM.yyyy");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateTime = LocalDate.parse(date, formatter);
        return currencyRepository.findByDate(dateTime);
    }

    @Override
    public Currency getCurrencyByCodeAndDate(String code, String date) {
        if (!checkDateFormat(date)) {
            throw new IllegalStateException("date: Tarix düzgün formatda daxil edilməyib. " +
                    "Düzgün format bu şəkildə olmalıdır: dd.MM.yyyy");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateTime = LocalDate.parse(date, formatter);
        Optional<Currency> currency = currencyRepository.findByCodeAndDate(code, dateTime);
        return currency.orElseGet(Currency::new);
    }

    private boolean checkDateFormat(String date) {
        String regex = "^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)\\d\\d$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
}